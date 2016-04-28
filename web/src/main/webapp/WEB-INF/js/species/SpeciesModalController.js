function SpeciesModalController($log, $scope, $rootScope, $filter, UserService, SpeciesService) {
    $scope.species = SpeciesService.getState().selectedSpecies;
    $scope.currentUser = UserService.getState().currentUser;
    $scope.varieties = SpeciesService.getAllVarieties();
    $scope.vernaculars = $filter('filter')(SpeciesService.getState().vernaculars, {speciesId: $scope.species.id}, true);
    $scope.speciesState = SpeciesService.getState();
    $scope.addVernacularName = SpeciesService.addVernacular;
    $scope.deleteVernacular = SpeciesService.deleteVernacular;
    $scope.updateSpecies = SpeciesService.updateSpecies;
    $scope.addVariety = SpeciesService.addVariety;
    $scope.selectSpecies = SpeciesService.selectSpecies;
    $scope.deleteSpecies = SpeciesService.deleteSpecies;


    $scope.toggleVariety = function (variety, species, event) {
        $log.log('Toggle variety:', [variety, species, event]);
        species.variety = species.variety == variety ? null : variety;
        event.currentTarget.blur();
    };
    $rootScope.$broadcast('species-modal-open', $scope.species);
    $scope.$on('modal.closing', function () {
        $rootScope.$broadcast('species-modal-close', $scope.species);
    });

}

angular.module('smigoModule').controller('SpeciesModalController', SpeciesModalController);