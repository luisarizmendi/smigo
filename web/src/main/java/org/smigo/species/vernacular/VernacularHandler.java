package org.smigo.species.vernacular;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 - 2016 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.smigo.species.CrudResult;
import org.smigo.species.Review;
import org.smigo.species.Species;
import org.smigo.species.SpeciesHandler;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class VernacularHandler {

    @Autowired
    private MailHandler mailHandler;
    @Autowired
    private SpeciesHandler speciesHandler;
    @Autowired
    private VernacularDao vernacularDao;


    public List<Vernacular> getVernacular(Locale locale) {
        return vernacularDao.getVernacular(locale);
    }

    public CrudResult addVernacular(Vernacular vernacular, AuthenticatedUser user, Locale locale) {
        Species species = speciesHandler.getSpecies(vernacular.getSpeciesId());
        List<Vernacular> currentVernaculars = vernacularDao.getVernacularBySpecies(species.getId());
        vernacular.setLanguage(locale.getLanguage());
        vernacular.setCountry(locale.getCountry());

        boolean isCreator = species.getCreator() == user.getId();
        if (isCreator || user.isModerator()) {
            vernacularDao.insertVernacular(vernacular);
            //ugly way of getting added vernacular  because h2 cant return multiple generated keys properly
            Vernacular added = currentVernaculars.stream().reduce((a, b) -> b.getVernacularName().equals(vernacular.getVernacularName()) ? b : a).get();
            return new CrudResult(added.getId(), Review.NONE);
        }

        mailHandler.sendReviewRequest("Add vernacular", currentVernaculars, vernacular, user);
        return new CrudResult(null, Review.MODERATOR);
    }

    public Review deleteVernacular(int vernacularId, AuthenticatedUser user, Locale locale) {
        Vernacular delete = vernacularDao.getVernacularById(vernacularId);
        Species species = speciesHandler.getSpecies(delete.getSpeciesId());

        boolean isCreator = species.getCreator() == user.getId();
        if (isCreator || user.isModerator()) {
            vernacularDao.deleteVernacular(vernacularId);
            return Review.NONE;
        }
        List<Vernacular> currentVernaculars = vernacularDao.getVernacularBySpecies(species.getId());
        mailHandler.sendReviewRequest("Delete vernacular", currentVernaculars, delete, user);

        return Review.MODERATOR;
    }

    public Review updateVernacular(int id, Vernacular update, AuthenticatedUser user, Locale locale) {
        Vernacular current = vernacularDao.getVernacularById(id);
        Species species = speciesHandler.getSpecies(current.getSpeciesId());

        update.setId(id);

        boolean isCreator = species.getCreator() == user.getId();
        if (isCreator || user.isModerator()) {
            vernacularDao.updateVernacular(update);
            return Review.NONE;
        }
        mailHandler.sendReviewRequest("Update vernacular", vernacularDao.getVernacularBySpecies(species.getId()), update, user);
        return Review.MODERATOR;
    }
}
