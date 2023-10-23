function adjustDateToUserTimeZone(utcDate) {
  var adjustedTime = new Date(utcDate);
  return`${adjustedTime.toLocaleTimeString()} ${adjustedTime.toLocaleDateString()}`;
}

function adjustAllDatesOnPageToUserTimeZone() {
  document.querySelectorAll('[data-original-date]').forEach(function (element) {
    var originalTime = element.getAttribute('data-original-date');
    element.textContent = adjustDateToUserTimeZone(originalTime);
  });
}

document.addEventListener('DOMContentLoaded', adjustAllDatesOnPageToUserTimeZone);
