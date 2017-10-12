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
  var app = angular.module('daobs');

  app.config(['$routeProvider', 'cfg',
    function ($routeProvider, cfg) {
      $routeProvider.when('/dashboard', {
        templateUrl: cfg.SERVICES.root +
        'app/components/dashboard/dashboardView.html'
      })
    }
  ]);

  app.controller('DashboardCtrl', ['$scope','cfg','$location', '$routeParams',
    function ($scope, cfg, $location, $routeParams) {
      if ($location.search().url) {
        $scope.dashboardURL = $location.search().url;
      } else {
        $scope.dashboardURL =
          $location.protocol() + '://' + $location.host() + ':'+$location.port() +
          cfg.SERVICES.dashboardBaseURL + cfg.defaultDashboard;
      }

      function setIframeSize() {
        const navbars = $('.navbar');
        $('#ifrm').css('height', (
          $(window).height() - $(navbars[0]).height() - $(navbars[1]).height()
        ) +'px');
        $('#ifrm').css('width', ($(window).width()) +'px');
      };

      $(window).resize(setIframeSize);

      setIframeSize();
    }]);

}());
