"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config", "restalchemy", function ExpensesCtrl($rootScope, $scope, $config, $restalchemy) {
	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

	var restExpenses = $restalchemy.init({ root: $config.apiroot }).at("expenses");

	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(expenses) {
			$scope.expenses = expenses;
			$scope.postError = null;
		});
	}

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
			var amountParts = $scope.newExpense.amount.split(" ");
			var amount = amountParts[0];
			var currency = amountParts.length > 1 ? amountParts[1] : "";
			// Post the expense via REST
			restExpenses.post({ date: $scope.newExpense.date, amount: amount, reason: $scope.newExpense.reason, currency: currency }).then(function() {
				// Reload new expenses list
				loadExpenses();
			}).error(function(e) {
				$scope.postError = e.message;	
			});
		}
	};

	$scope.clearExpense = function() {
		$scope.newExpense = {};
		$scope.postError = null;
	};

	// Initialise scope variables
	loadExpenses();
	$scope.clearExpense();
}]);
