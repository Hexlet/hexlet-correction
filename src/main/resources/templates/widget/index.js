document.addEventListener('DOMContentLoaded', function () {
  const popup = document.getElementById('popup');
  const popupClose = document.querySelector('.popup__close');
  const popupBody = document.querySelector('.popup__body');
  const popupContent = document.querySelector('.popup__content');
  const btnClose = document.querySelector('.btn-secondary');
  const form = document.querySelector('.total-form');
  const pageUrl = document.getElementById('pageUrl');
  const textBeforeTypo = document.getElementById('textBeforeTypo');
  const textTypo = document.getElementById('textTypo');
  const textAfterTypo = document.getElementById('textAfterTypo');

  const state = {
    text: '',
    before: '',
    comment: '',
  }
  document.addEventListener('keydown', function (e) {
    if (e.keyCode === 13 && e.ctrlKey) {
      state.text = window.getSelection().toString();
      if (state.text.length) {
        e.preventDefault();
        popup.classList.add('shown');
        const range = new Range();
        pageUrl.value = range.startContainer.baseURI;
        // state.before = `${window
        //   .getSelection()
        //   .focusNode.textContent
        //   .split(state.text)[0]
        //   .slice(-50)}${state.text}${window
        //   .getSelection()
        //   .focusNode.textContent
        //   .split(state.text)[1]
        //   .slice(50)}`;
        state.before = window.getSelection().focusNode.textContent;
        textBeforeTypo.value = state.before;
        textBeforeTypo.disabled = 'disabled';
        textTypo.value = state.text;
        textTypo.disabled = 'disabled';
        textAfterTypo.value = state.text;
        textAfterTypo.classList.add('outline-red');
        textAfterTypo.addEventListener('input', (e) => {
          e.preventDefault();
          if (textTypo.value !== textAfterTypo.value) {
            textAfterTypo.classList.remove('outline-red');
          } else {
            textAfterTypo.classList.add('outline-red');
          }
        })
      }
    }
  })
  const closePopup = () => {
    popup.classList.remove('shown');
    form.reset();
  }
  popupClose.addEventListener('click', closePopup);
  popupBody.addEventListener('click', closePopup);
  btnClose.addEventListener('click', closePopup);
  popupContent.addEventListener('click', event => event.stopPropagation());

});
