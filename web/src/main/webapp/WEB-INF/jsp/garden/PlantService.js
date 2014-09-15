function PlantService($http, $window, $timeout, $rootScope, InitService) {
    var yearSquareMap = InitService.garden.squares,
        unsavedCounter = 0,
        autoSaveInterval = 60000,
        timedAutoSavePromise = $timeout(sendUnsavedPlantsToServer, autoSaveInterval, false);
    console.log('PlantService', [yearSquareMap]);

    $window.onbeforeunload = function () {
        sendUnsavedPlantsToServer();
        return null;
    };


    function PlantData(plant) {
        this.year = plant.location.year;
        this.y = plant.location.y;
        this.x = plant.location.x;
        this.speciesId = plant.species.id;
    }

    function Location(year, x, y) {
        if (!$.isNumeric(year) || !$.isNumeric(x) || !$.isNumeric(y)) {
            throw "Location takes Numeric parameter only. x:" + x + " y:" + y + " year:" + year;
        }
        this.year = +year;
        this.x = +x;
        this.y = +y;
    }

    function Plant(species, location, flag) {
        this.species = species;
        this.location = location;
        if (flag) {
            this[flag] = true;
        }
    }

    function Square(location, speciesArray, flag) {
        if (!location instanceof Location) {
            throw "Square.location must be a Location object. location:" + location;
        }
        if (!speciesArray instanceof Array) {
            throw "Square.speciesArray must be an Array. speciesArray:" + speciesArray;
        }
        this.location = location;
        this.plants = {};
        if (speciesArray) {
            speciesArray.forEach(function (species) {
                if (species.id) {
                    console.error('Can not add species with no id', species);
                } else {
                    this.plants[species.id] = new Plant(species, location, 'add');
                }
            }, this);
        }
        if (flag) {
            this[flag] = true;
        }
    }

    function getBounds(year) {
        var axisLength = 9999;
        var ret = {
            xmax: -axisLength,
            ymax: -axisLength,
            xmin: axisLength,
            ymin: axisLength
        };
        [year, getTrailingYear(year)].forEach(function (year) {
            angular.forEach(yearSquareMap[year], function (square, index) {
                ret.xmax = Math.max(square.location.x, ret.xmax);
                ret.ymax = Math.max(square.location.y, ret.ymax);
                ret.xmin = Math.min(square.location.x, ret.xmin);
                ret.ymin = Math.min(square.location.y, ret.ymin);
            });
        });
        return ret;
    }

    function getTrailingYear(year) {
        var yearSource = year;
        if (yearSquareMap[yearSource - 1]) {
            return yearSource - 1;
        } else if (yearSquareMap[yearSource + 1]) {
            return yearSource + 1;
        } else {
            return Object.keys(yearSquareMap).sort().slice(-1);
        }
    }

    function getAvailableYears() {
        var sortedYearsArray = Object.keys(yearSquareMap).sort();
        var firstYear = +sortedYearsArray[0];
        var lastYear = +sortedYearsArray.slice(-1)[0];
        var ret = [];
        for (var i = firstYear; i <= lastYear; i++) {
            ret.push(i);
        }
        console.log('AvailableYears', ret);
        return ret;
    }

    function sendUnsavedPlantsToServer() {
        //reset counter
        unsavedCounter = 0;
        $timeout.cancel(timedAutoSavePromise);
        //get unsaved plants
        var update = { addList: [], removeList: [] };
        angular.forEach(yearSquareMap, function (squareList) {
            angular.forEach(squareList, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (plant.add && !plant.remove) {
                        update.addList.push(new PlantData(plant));
                        delete plant.add;
                        delete plant.remove;
                    } else if (plant.remove && !plant.add) {
                        update.removeList.push(new PlantData(plant));
                        delete square.plants[plant.species.id];
                    }
                });
            });
        });
        console.log('Sending to server', update);
        //start auto save timer
        timedAutoSavePromise = $timeout(sendUnsavedPlantsToServer, autoSaveInterval, false);
        return $http.post('rest/garden', update);
    }

    function countAutoSave() {
        unsavedCounter++;
        if (unsavedCounter > 13) {
            sendUnsavedPlantsToServer();
        }
    }

    $rootScope.$on('newGardenAvailable', function (event, garden) {
        yearSquareMap = garden.squares;
    });

    return {
        getSquares: function (year) {
            return yearSquareMap[year];
        },
        getBounds: getBounds,
        addYear: function (year, model) {
            var newYearSquareArray = [];
            var copyFromSquareArray = yearSquareMap[getTrailingYear(year)];

            //add perennial from mostRecentYear
            angular.forEach(copyFromSquareArray, function (square) {
                angular.forEach(square.plants, function (plant) {
                    if (!plant.species.annual) {
                        var newLocation = new Location(year, plant.location.x, plant.location.y);
                        newYearSquareArray.push(new Square(newLocation, [plant.species]));
                    }
                });
            });

            yearSquareMap[year] = newYearSquareArray;
            model.selectedYear = year;
            model.availableYears = getAvailableYears();
            console.log('Year added:' + year, yearSquareMap);
        },
        getAvailableYears: getAvailableYears,
        addSquare: function (year, x, y, species) {
            var newSquare = new Square(new Location(year, x, y), [species]);
            yearSquareMap[year].push(newSquare);
            countAutoSave();
            console.log('Square and plant added', newSquare);
            return newSquare;
        },
        removePlant: function (square) {
            angular.forEach(square.plants, function (plant, key) {
                if (plant.add) {//undo add
                    delete square.plants[key];
                    console.log('Undo add', plant);
                } else {
                    plant.remove = true;
                    console.log('Plant removed', plant);
                }
            });
            console.log('Plant(s) removed', square);
            countAutoSave();
        },
        addPlant: function (species, square) {
            if (!species.id) {
                console.error('Can not add species with no id', species);
                return;
            }
            if (!square.plants[species.id]) {
                square.plants[species.id] = new Plant(species, square.location, 'add');
                console.log('Plant added: ' + species.scientificName, square);
                countAutoSave();
            } else {//undo remove
                delete square.plants[species.id].remove;
                console.log('Undo remove', square);
            }
        },
        save: sendUnsavedPlantsToServer
    };
}
angular.module('smigoModule').factory('PlantService', PlantService);