document.addEventListener('DOMContentLoaded', function () {
  const popup = document.getElementById('popup');
  const popupClose = document.querySelector('.popup__close');
  const popupBody = document.querySelector('.popup__body');
  const popupContent = document.querySelector('.popup__content');
  const btnClose = document.querySelector('.btn-secondary');
  const form = document.querySelector('.total-form');
  const textBeforeTypo = document.getElementById('textBeforeTypo');
  const textTypo = document.getElementById('textTypo');
  const textAfterTypo = document.getElementById('textAfterTypo');

  function getSelectedText() {
    if (window.getSelection()) {
      const select = window.getSelection();
      return select.toString();
    }
  }

  document.addEventListener('keydown', function (e) {
    const text = getSelectedText();
    if ((e.keyCode === 13 && e.ctrlKey) && text) {
      e.preventDefault();
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
