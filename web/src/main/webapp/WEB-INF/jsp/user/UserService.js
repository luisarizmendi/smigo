function UserService($rootScope, Http, $location, PlantService, $q, GardenService) {

    var anonymousUser = {username: '', termsOfService: true};

    $rootScope.currentUser = InitService.user;

    $rootScope.$on('current-user-changed', function (event, user) {
        $rootScope.currentUser = user;
    });

    function validateForm(form) {
        form.objectErrors = [];
        var deferred = $q.defer();
        if (form.$invalid) {
            //set all values to trigger dirty, so validation messages become visible
            angular.forEach(form, function (value) {
                if (value.$setViewValue) {
                    value.$setViewValue(value.$viewValue);
                }
            });
//            console.log('Form is invalid', form);
            deferred.reject('Form is invalid');
        } else {
            deferred.resolve();
        }
        return deferred.promise;
    }

    function login(form, formModel) {
        console.log('Login');
        formModel['remember-me'] = true;
        return validateForm(form)
            .then(function () {
                return Http.post('login', formModel);
            })
            .then(function (response) {
                console.log('Login success, response:', response);
                $rootScope.$broadcast('current-user-changed', response.data);
            })
            .then(GardenService.reloadGarden)
            .then(function () {
                $location.path('/garden');
            }).catch(function (errorReason) {
                console.log('Login failed, reason:', errorReason);
                form.objectErrors = errorReason.data;
            });
    }

    return {
        register: function (form, formModel) {
            validateForm(form)
                .then(PlantService.save)
                .then(function () {
                    console.log('Registering');
                    return Http.post('register', formModel);
                })
                .then(function () {
                    return login(form, formModel);
                }).catch(function (errorReason) {
                    console.log('Login failed', errorReason);
                    form.objectErrors = errorReason.data;
                });
        },
        login: login,
        acceptTermsOfService: function (form) {
            console.log('Handle acceptTermsOfServiceForm', form);
            if (form.$invalid) {
                return;
            }
            $('#accept-terms-of-service-modal').modal('hide');
            Http.post('accept-terms-of-service', {});
        },
        logout: function () {
            Http.get('logout')
                .then(function () {
                    $rootScope.$broadcast('current-user-changed', anonymousUser);
                    $rootScope.$broadcast('newGardenAvailable', {species: {}, squares: {}});
                })
                .then(GardenService.reloadGarden)
                .catch(function (data, status, headers, config) {
                    console.error('Logout failed', data, status, headers, config);
                });

        }
    };
}
angular.module('smigoModule').factory('UserService', UserService);