/**
 * Kitchen garden aid is a planning tool for kitchengardeners.
 * Copyright (C) 2010 Christian Nilsson
 *
 * This file is part of Kitchen garden aid.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * Email contact christian1195@gmail.com
 */

package org.sourceforge.kga.rules;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sourceforge.kga.Species;
import org.sourceforge.kga.errors.RuleException;

/**
 * This rule gives a hint when planting a beneficial plant next to each other.
 * For instance onion next to carrot.
 *
 * @author Christian Nilsson
 */

public class FightDiseaseRule extends AbstractBeneficialRule {
  private static final Logger log = LoggerFactory.getLogger(ImproveFlavorRule.class);


  public FightDiseaseRule(Species host, Species friend) throws RuleException {
    super(host, friend);
  }

  @Override
  public RuleType getRuleType() {
    return RuleType.fightdisease;
  }

  @Override
  public String getHintTranslationKey() {
    return "hint.fightdisease";
  }
}