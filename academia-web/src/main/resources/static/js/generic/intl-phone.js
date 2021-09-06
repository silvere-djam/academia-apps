$("#telephone").intlTelInput();
// destroy
$("#telephone").intlTelInput("destroy");
 
// Get the extension part of the current number
var extension = $("#telephone").intlTelInput("getExtension");
 
// Get the current number in the given format
var intlNumber = $("#telephone").intlTelInput("getNumber");
 
// Get the type (fixed-line/mobile/toll-free etc) of the current number.
var numberType = $("#telephone").intlTelInput("getNumberType");
 
// Get the country data for the currently selected flag.
var countryData = $("#telephone").intlTelInput("getSelectedCountryData");
 
// Get more information about a validation error.
var error = $("#telephone").intlTelInput("get<a href="http://www.jqueryscript.net/tags.php?/Validation/">Validation</a>Error");
 
// Vali<a href="http://www.jqueryscript.net/time-clock/">date</a> the current number
var isValid = $("#telephone").intlTelInput("isValidNumber");
 
// Load the utils.js script (included in the lib directory) to enable formatting/validation etc.
$("#telephone").intlTelInput("loadUtils", "lib/libphonenumber/build/utils.js");
 
// Change the country selection
$("#telephone").intlTelInput("selectCountry", "gb");
 
// Insert a number, and update the selected flag accordingly.
$("#telephone").intlTelInput("setNumber", "+44 7733 123 456");
