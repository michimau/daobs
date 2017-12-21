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
(function () {
  "use strict";
  /*jslint todo: true */
  /**
   *
   * @type {module|*}
   */
  var app = angular.module('daobs', [
    'daobs_cfg',
    'user_service',
    'login',
    'ngRoute',
    'pascalprecht.translate',
    'csw',
    'ui-notification']);

  app.controller('RootController', [
    '$scope', '$location', '$http', 'cfg', 'userService', '$filter',
    function ($scope, $location, $http, cfg, userService, $filter) {
      $scope.userInfoChecked = false;
      userService.getCurrentUserInfo().finally(function() {
        $scope.userInfoChecked = true;
      });

      $scope.getUser = function() {
        return userService.getUser();
      };

      $scope.config = cfg;

      $scope.navLinks = [{
        id: 'home',
        icon: 'fa-home',
        url: '#/',
        needsLogin: false
      }, {
        id: 'dashboard',
        icon: 'fa-bar-chart',
        // TODO: Should be displayed only if dashboard available
        // TODO: Should point to a dashboard that exist
        //url: cfg.SERVICES.dashboardBaseURL + cfg.defaultDashboard,
        url: '#/dashboard',
        needsLogin: true
      }, {
        id: 'monitoring',
        icon: 'fa-list-alt',
        url: '#/monitoring/manage',
        needsLogin: false
      }, {
        id: 'harvesting',
        icon: 'fa-cloud-download',
        url: '#/harvesting/manage',
        needsLogin: false
      }];

      $scope.getNavLinks = function() {
        if (!userService.getUser() || !userService.getUser().authenticated) {
          return $filter('filter')($scope.navLinks, {needsLogin: false});
        } else {
          return $scope.navLinks;
        }
      };


      $scope.$on('$locationChangeSuccess', function() {
        $scope.redirectLocation = encodeURIComponent(window.location.href);
      });


      // Change class based on route path
      $scope.currentRoute = null;
      $scope.navClass = function (page) {
        var path = $location.path().replace('/', '');
        $scope.currentRoute = path || 'home';
        return page.replace('#/', '') === $scope.currentRoute ||
        $scope.currentRoute.indexOf(page.replace('#/', '') + "/") === 0 ? 'active' : '';
      };

      $scope.startIntro = function () {
        var intro = introJs();
        $http.get(cfg.SERVICES.root +
          'assets/introConfig.json').success(function (data) {
          var items = data.steps.menu;
          items.push.apply(items, data.steps[$scope.currentRoute]);
          intro.setOptions({steps: items});
          intro.start();
        });
      };
    }]);

  app.controller('LogoutCtrl', ['$http', '$log', '$window', 'cfg',
    function ($http, $log, $window, cfg) {
      var outcome;

      var logout = function () {
        return $http.post(cfg.SERVICES.root + "logout", {
          cache: false
        }).then(function () {
          window.location = cfg.SERVICES.root;
        }, function () {
          $log.warn("Error exiting from Solr or the app");
        });
      };

      try {
        // Logout from kibana
        // IE has a simple solution for it - API:
        outcome = document.execCommand('ClearAuthenticationCache');
      } catch(e){}
      if (!outcome) {
        var randomPassword = Math.floor(Math.random() * (1000000000 - 100000000));
        var nonExistentAuth = $window.btoa('logoutUser:' + randomPassword);
        jQuery.ajax({
          type: "GET",
          url: cfg.SERVICES.dashboardApiBulkGet,
          async: false,
          username: "logoutUser",
          password: "" + randomPassword,
          headers: { "Authorization": "Basic " + nonExistentAuth }
        })
          .done(function(){
            // If we don't get an error, we actually got an error as we expect an 401!
          })
          .fail(function(){
            // We expect to get an 401 Unauthorized error! In this case we are successfully
            // logged out and we redirect the user.
            logout();
          });
      }
    }]);
}());
