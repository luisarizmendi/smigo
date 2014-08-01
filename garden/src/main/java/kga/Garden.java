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

package kga;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import kga.errors.GardenException;

import java.util.*;

public class Garden implements Iterable<SquareIterator> {

    private Map<YearXY, Square> squares;

    public Garden(Map<Integer, ? extends Species> species, List<PlantData> plants) {
        squares = new HashMap<YearXY, Square>();
        if (plants != null) {
            for (PlantData p : plants) {
                Species s = species.get(p.getSpeciesId());
                this.addOrGetSquare(p.getYear(), p.getX(), p.getY()).addSpecies(s);
            }
        }
    }

    public Garden() {
        this(new HashMap<Integer, Species>(), new ArrayList<PlantData>());
    }

    /**
     * Add new year to garden. Copies items and perennials from latest year to
     * the new year.
     */
    public void addYear(int newYear) {
        SortedSet<Integer> years = getYears();
        if (years.contains(newYear))
            throw new GardenException("Cant add year since year " + newYear + " already exists", "AddYear.YearAlreadyExists");
        copyPermanentSquares(years.last(), newYear);
        if (getPlants(newYear).isEmpty())
            throw new GardenException("No plants added ", "AddYear.NoItemsOrPerennials");
    }

    public List<Plant> getPlants(int year) {
        List<Plant> ret = new ArrayList<Plant>();
        for (Square s : squares.values())
            if (s.getYear() == year)
                ret.addAll(s.getPlants());
        return ret;
    }

    public List<Plant> getPlants() {
        List<Plant> ret = new ArrayList<Plant>();
        for (Square s : squares.values())
            ret.addAll(s.getPlants());
        return ret;
    }

    /**
     * Copies square for one year to another. Copies only perennial and items.
     * Annuals are not copied.
     *
     * @param fromYear existing year that squares are copied from
     * @param toYear   new year to which square are copied to
     */
    private void copyPermanentSquares(int fromYear, int toYear) {
        Collection<Square> copyOfSquares = new ArrayList<Square>(squares.values());
        for (Square s : copyOfSquares)
            if (s.isYear(fromYear)) {
                Square copy = new Square(toYear, s.getX(), s.getY(), s.getPerennialSpecies());
                if (!copy.getPlants().isEmpty())
                    squares.put(s.getYearXY(), copy);
            }
    }

    /**
     * Returns a sorted set of all the year available in this garden.
     *
     * @return a sorted set of all the years
     */
    public SortedSet<Integer> getYears() {
        SortedSet<Integer> years = new TreeSet<Integer>();
        if (squares != null && squares.size() != 0)
            for (Square s : squares.values())
                years.add(s.getYear());
        return years;
    }

    public String toString() {
        return "Garden with " + squares.size() + " squares";
    }

    /**
     * Adds a square at year and grid. If there already is a square at this
     * location it will only return this square.
     *
     * @return the added or existing square
     */
    public Square addOrGetSquare(int year, int x, int y) {
        // log.info("Add square at " + grid);
        Square newSquare = getSquare(year, x, y);
        // if square already exist return that one
        if (newSquare != null)
            return newSquare;
        newSquare = new Square(year, x, y);
        squares.put(newSquare.getYearXY(), newSquare);
        return newSquare;
    }

    /**
     * Returns square at year and location or null if not present.
     */
    public Square getSquare(int year, int x, int y) {
        return squares.get(new YearXY(year, x, y));
    }

    /**
     * Returns all squares for specified year.
     *
     * @param year the year
     * @return squares for specified year
     */
    public List<Square> getSquaresFor(Integer year) {
        List<Square> squaresForYear = new ArrayList<Square>();
        for (Square s : squares.values())
            if (s.isYear(year))
                squaresForYear.add(s);
        return squaresForYear;
    }

    /**
     * Deletes whole year from garden.
     *
     * @param year the year of which squares will be removed
     */
    public void deleteYear(int year) {
        for (Square s : getSquaresFor(year))
            squares.remove(s.getYearXY());
    }

    public void deleteYears(Set<Integer> years) {
        for (Integer y : years)
            deleteYear(y);
    }

    /**
     * Returns the bounds
     *
     * @param year
     * @return the bounds
     */
    public Bounds getBoundsFor(int year) {
        Collection<Square> squaresAtYear = getSquaresFor(year);
        squaresAtYear.add(addOrGetSquare(year, 0, 0));
        if (getPlants(year).isEmpty())
            return new Bounds(-6, -3, 6, 3);
        Bounds ret = new Bounds(-2, -2, 2, 2);
        for (Square s : squaresAtYear) {
            if (!s.getPlants().isEmpty()) {
                ret.setMiny(Math.min(s.getY(), ret.getMiny()));
                ret.setMinx(Math.min(s.getX(), ret.getMinx()));
                ret.setMaxx(Math.max(s.getX(), ret.getMaxx()));
                ret.setMaxy(Math.max(s.getY(), ret.getMaxy()));
            }
        }
        ret.increseBounds();
        return ret;
    }

    public Map<Integer, Collection<Square>> getSquares() {
        Multimap<Integer, Square> ret = ArrayListMultimap.create();
        for (Square square : squares.values()) {
            ret.put(square.getYear(), square);
        }
        return ret.asMap();
    }

    @Override
    public Iterator<SquareIterator> iterator() {
        return new YearIterator(this);
    }

    public Iterator<SquareIterator> getIterator() {
        return iterator();
    }

    public boolean hasSpecies(int id) {
        for (Square s : squares.values()) {
            for (Plant p : s.getPlants()) {
                if (p.getSpecies().getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the eight nearest neighbors plus itself.
     *
     * @param location
     * @return the eight nearest neighbors plus itself
     */
    public Collection<Square> getSurroundingSquares(Location location) {
        return getSurroundingSquares(location, 1);
    }

    /**
     * Returns neighboring squares plus itself.
     *
     * @param location
     * @param radius   the radius (radius = 1 gives 9 squares, radius = 2 gives 25
     *                 squares)
     * @return neighboring squares plus itself
     */
    public Collection<Square> getSurroundingSquares(Location location, int radius) {
        Collection<Square> surrounding = new HashSet<Square>();
        for (Square s : squares.values()) {
            int xDiff = Math.abs(location.getX() - s.getX());
            int yDiff = Math.abs(location.getY() - s.getY());
            if (s.getYear() == location.getYear() && xDiff <= radius && yDiff <= radius)
                surrounding.add(s);
        }
        return surrounding;
    }

    /**
     * Returns previous squares at same location.
     *
     * @param location
     * @param yearsBack years back in time.
     * @return previous squares at same location
     */
    public Collection<Square> getPreviousSquares(Location location, int yearsBack) {
        Collection<Square> previous = new HashSet<Square>();
        int earliestYear = location.getYear() - yearsBack;
        for (Square s : squares.values()) {
            if (s.getYear() < location.getYear() && s.getYear() >= earliestYear
                    && s.getX() == location.getX() && s.getY() == location.getY()) {
                previous.add(s);
            }
        }
        return previous;
    }

    /**
     * Returns neighboring squares and theirs previous squares.
     */
    public Collection<Square> getPreviousSurroundingSquares(Location location, int yearsBack, int radius) {
        Collection<Square> ret = new HashSet<Square>();
        int earliestYear = location.getYear() - yearsBack;
        for (Square s : squares.values()) {
            int xDiff = Math.abs(location.getX() - s.getX());
            int yDiff = Math.abs(location.getY() - s.getY());
            if (s.getYear() < location.getYear() && s.getYear() > earliestYear && xDiff <= radius && yDiff <= radius)
                ret.add(s);
        }
        return ret;
    }

    public List<Hint> getHints() {
        List<Hint> ret = new ArrayList<Hint>();
        for (Square square : squares.values()) {
            for (Plant plant : square.getPlants()) {
                ret.addAll(plant.getHints(this));
            }
        }
        return ret;
    }
}