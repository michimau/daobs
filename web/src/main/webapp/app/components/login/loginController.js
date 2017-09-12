/*
 * Copyright 2014-2016 European Environment Agency
 *
 * Licensed under the EUPL, Version 1.1 or – as soon
 * they will be approved by the European Commission -
 * subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance
 * with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
(function() {
  "use strict";
  var app = angular.module('login');

  app.controller('LoginController', [
                      '$scope', 'userService', '$timeout', 'cfg', '$location',
                      function($scope, userService, $timeout, cfg, $location) {
    $scope.loginObj = {
      redirect: $location.search().redirect || ''
    };


    $scope.signIn = function() {
      $scope.loginProcessing = true;
      $scope.loginError = false;
      userService.login(
        $scope.loginObj.username,
        $scope.loginObj.password,
        $scope.loginObj.redirect).then(
        function(data){
          $timeout(function() {
            window.location =
              $location.search().redirect || cfg.SERVICES.root;
            window.location.reload();
          }, 100);

        },
        function(rejection) {
          $scope.loginError = true;
        }
      ).finally(function() {
        $scope.loginProcessing = false;
      });
    }

  }]);

})();
