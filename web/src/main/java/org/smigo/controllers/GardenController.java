package org.smigo.controllers;

import kga.Garden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.CurrentUser;
import org.smigo.entities.PlantDataBean;
import org.smigo.formbean.AddYearFormBean;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.smigo.species.SpeciesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

/**
 * Controller that handles Garden specific type of requests.
 *
 * @author Christian Nilsson
 */
@Controller
@RequestMapping("/")
public class GardenController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private DatabaseResource databaseResource;
    @Autowired
    private UserSession userSession;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SpeciesHandler speciesHandler;

    public GardenController() {
        log.debug("Creating new GardenController");
    }

    @RequestMapping(value = {"/garden", "/"}, method = RequestMethod.GET)
    public String getGarden() {
        SortedSet<Integer> years = speciesHandler.getGarden().getYears();
        int current = Calendar.getInstance().get(Calendar.YEAR);
        return "forward:/garden/" + (years.isEmpty() ? current : years.last());
    }

    @RequestMapping(value = "/garden/{year}")
    public String getGarden(@PathVariable Integer year, Model model) {
        Garden g = speciesHandler.getGarden();
        log.debug("Displaying garden: " + year + ", squares:" + g.getAllSquares().size() + ", bounds:" + g.getBoundsFor(year));
        model.addAttribute("year", year);
        model.addAttribute("kgagarden", g);
        model.addAttribute("specieslist", speciesHandler.getVisibleSpecies());
        model.addAttribute("speciesJson", speciesHandler.getVisibleSpecies());
        return "garden.jsp";
    }

    @RequestMapping(value = "/savegarden", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String saveGarden(@RequestBody List<PlantDataBean> plants, Locale locale) {
        log.debug("Savegarden. Plants:" + plants.size());
        speciesHandler.updateGarden(plants);
        String loginFirst = messageSource.getMessage("account.pleaseloginfirst", null, locale);
        String ok = messageSource.getMessage("ok", null, locale);
        return currentUser.isAuthenticated() ? ok : loginFirst;
    }

    @RequestMapping(value = "/addyear", method = RequestMethod.GET)
    public String getAddYearForm(Model model) {
        model.addAttribute("addYearFormBean", new AddYearFormBean());
        return "addyearform.jsp";
    }

    @RequestMapping(value = "/addyear", method = RequestMethod.POST)
    public String handleAddYearForm(@Valid AddYearFormBean addYearFormBean, BindingResult result) {
        final Integer year = addYearFormBean.getYear();
        log.debug("Adding year " + year);
        if (result.hasErrors()) {
            return "addyearform.jsp";
        }
        speciesHandler.addYear(year);
        return "redirect:garden/" + year;
    }

    @RequestMapping(value = "/deleteyear", method = RequestMethod.GET)
    public String getDeleteYearForm() {
        return "deleteyearform.jsp";
    }

    @RequestMapping(value = "/deleteyear", method = RequestMethod.POST)
    public String handleDeleteYearForm(@RequestParam("deleteyear") Integer deleteyear) {
        log.debug("Delete year " + deleteyear);
        databaseResource.deleteYear(currentUser.getId(), deleteyear);
        return "redirect:garden";
    }

    @RequestMapping(value = "/referrer", method = RequestMethod.POST)
    @ResponseBody
    public void saveReferrer(@RequestBody String referrer) {
        log.info("document.referrer: " + referrer);
    }

}