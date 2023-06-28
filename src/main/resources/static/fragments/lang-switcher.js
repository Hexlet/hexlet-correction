function getCurrentLanguage() {
  return getCookie('lang') || 'en';
}

// update dropdown-toggle value
function updateLanguageDropdown() {
  var currentLanguage = getCurrentLanguage();
  var languageDropdown = document.getElementById('languageDropdown');
  var languageText = document.getElementById('languageText');
  languageDropdown.textContent = currentLanguage.toUpperCase();
}

// init dropdown-toggle value on page load
document.addEventListener('DOMContentLoaded', function() {
  updateLanguageDropdown();
});

function switchLanguage(lang) {
  var url = window.location.href.split('?')[0]; // get current URL without params
  window.location.href = url + '?lang=' + lang; // add param ?lang= and redirect to the current page
}

function getCookie(name) {
  let cookie = {};
  document.cookie.split(';').forEach(function(el) {
    let [k,v] = el.split('=');
    cookie[k.trim()] = v;
  })
  return cookie[name];
}
