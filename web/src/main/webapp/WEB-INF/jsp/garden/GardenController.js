function GardenController($scope, PlantService, SpeciesService, GardenService) {

    $scope.$watch('model.selectedYear', function (toYear, fromYear) {
        console.log('Selected year changed', toYear, fromYear);
        $scope.squares = PlantService.getSquares(toYear);
        $scope.visibleRemainderSquares = PlantService.getSquares(toYear - 1);
    });

    $scope.$watch('model.availableYears', function (newYears) {
        $scope.forwardYear = newYears.slice(-1).pop() + 1;
        $scope.backwardYear = newYears[0] - 1;
    });

    $scope.species = SpeciesService.getSpecies();
    $scope.addYear = PlantService.addYear;
    $scope.model = GardenService.model;
    $scope.onSquareClick = GardenService.onSquareClick;
    $scope.onVisibleRemainderClick = GardenService.onVisibleRemainderClick;
    $scope.onGridClick = GardenService.onGridClick;
    $scope.getGridSizeCss = GardenService.getGridSizeCss;
    $scope.getSquarePositionCss = GardenService.getSquarePositionCss;
}

angular.module('smigoModule').controller('GardenController', GardenController);