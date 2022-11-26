document.addEventListener('DOMContentLoaded', function () {
  const popup = document.getElementById('popup');
  const popupClose = document.querySelector('.popup__close');
  const popupBody = document.querySelector('.popup__body');
  const popupContent = document.querySelector('.popup__content');
  const btnClose = document.querySelector('.btn-secondary');
  const form = document.querySelector('.total-form');

  document.addEventListener('keydown', function(e) {
    e.preventDefault();
    if ((e.keyCode === 13 && e.ctrlKey) && popup) {
      popup.classList.add('shown');
    }
});
  const closePopup = () => {
    popup.classList.remove('shown');
    form.reset();
  }
  popupClose.addEventListener('click', closePopup);
  popupBody.addEventListener('click', closePopup);
  btnClose.addEventListener('click', closePopup);
  popupContent.addEventListener('click', event => event.stopPropagation());

});
