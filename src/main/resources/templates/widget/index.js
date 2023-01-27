document.addEventListener('DOMContentLoaded', function () {
  const state = {
    text: '',
    before: '',
    after: 'sss',
    comment: '',
  }

  function getUnselectedText(containerEl) {
    let sel, range, tempRange, before = "", after = "";
    if (typeof window.getSelection != "undefined") {
      sel = window.getSelection();
      if (sel.rangeCount) {
        range = sel.getRangeAt(0);
      } else {
        range = document.createRange();
        range.collapse(true);
      }
      tempRange = document.createRange();
      tempRange.selectNodeContents(containerEl);
      tempRange.setEnd(range.startContainer, range.startOffset);
      before = tempRange.toString();

      tempRange.selectNodeContents(containerEl);
      tempRange.setStart(range.endContainer, range.endOffset);
      after = tempRange.toString();

      tempRange.detach();
    } else if ((sel = document.selection) && sel.type != "Control") {
      range = sel.createRange();
      tempRange = document.body.createTextRange();
      tempRange.moveToElementText(containerEl);
      tempRange.setEndPoint("EndToStart", range);
      before = tempRange.text;

      tempRange.moveToElementText(containerEl);
      tempRange.setEndPoint("StartToEnd", range);
      after = tempRange.text;
    }
    return {
      before: before.replaceAll('\n', '').replaceAll('\t', '').trim().slice(-50),
      after: after.replaceAll('\n', '').replaceAll('\t', '').trim().slice(0, 50)
    };
  }
  document.head.innerHTML += '<link rel="stylesheet" href="index.css" type="text/css"/>';
  document.addEventListener('keydown', function (e) {
    if(e.keyCode !== 13 || !e.ctrlKey) {return}
    state.text = window.getSelection().toString();
    if (!window.getSelection().isCollapsed) {
      e.preventDefault();
      // create iframe
      const iframe = document.createElement('iframe');
      iframe.setAttribute('src', 'typo-form.html');
      iframe.classList.add('hc-iframe');
      // create popup
      const popup = document.createElement('div');
      popup.id = 'popup';
      popup.className = 'hc-popup';
      popup.innerHTML = `<div class="hc-popup__body">
        <div class="hc-popup__content hc-shadow">
          <div data-name="popup-close" class="hc-popup__close">
            <svg width="20px" height="20px" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
              <g data-name="Layer 2">
                <g data-name="close">
                  <rect width="20" height="20" transform="rotate(180 12 12)" opacity="0"/>
                  <path fill="#212529"
                        d="M13.41 12l4.3-4.29a1 1 0 1 0-1.42-1.42L12 10.59l-4.29-4.3a1 1 0 0 0-1.42 1.42l4.3 4.29-4.3 4.29a1 1 0 0 0 0 1.42 1 1 0 0 0 1.42 0l4.29-4.3 4.29 4.3a1 1 0 0 0 1.42 0 1 1 0 0 0 0-1.42z"/>
                </g>
              </g>
            </svg>
          </div>
          <div class="hc-popup__title">Отправить сообщение об ошибке?</div>
          <div class="hc-popup__iframe">
          </div>
        </div>
      </div>`
      popup.querySelector('.hc-popup__iframe').appendChild(iframe);
      const closePopup = () => {
        popup.remove();
        window.getSelection().removeAllRanges();
      }
      const popupClose = popup.querySelector('.hc-popup__close');
      const popupBody = popup.querySelector('.hc-popup__body');
      const popupContent = popup.querySelector('.hc-popup__content');
      iframe.onload = function () {
        console.log("iframe загрузился");
        const pageUrl = iframe.contentWindow.document.body.querySelector('#pageUrl');
        const textBeforeTypo = iframe.contentWindow.document.body.querySelector('#textBeforeTypo');
        const textTypo = iframe.contentWindow.document.body.querySelector('#textTypo');
        const textAfterTypo = iframe.contentWindow.document.body.querySelector('#textAfterTypo');
        const reporterName = iframe.contentWindow.document.body.querySelector('#reporterName');
        const commentTextarea = iframe.contentWindow.document.body.querySelector('#commentTextarea');
        const btn = iframe.contentWindow.document.body.querySelector('.hc-btn-secondary');
        const range = new Range();
        pageUrl.value = range.startContainer.baseURI;
        state.before = getUnselectedText(document.body).before;
        state.after = getUnselectedText(document.body).after;
        textBeforeTypo.value = `${state.before} ${state.text} ${state.after}`;
        textBeforeTypo.disabled = 'disabled';
        iframe.contentWindow.document.body.querySelector('.text-before-typo').textContent = state.before;
        iframe.contentWindow.document.body.querySelector('.text-typo').textContent = state.text;
        iframe.contentWindow.document.body.querySelector('.text-after-typo').textContent = state.after;

        function resize() {
          iframe.height = iframe.contentWindow.document.body.scrollHeight;
        }

        resize();

        commentTextarea.addEventListener('input', (e) => {
          e.preventDefault();
          state.comment = e.target.value;
        })
        reporterName.value = state.comment;
        textTypo.value = state.text;
        textTypo.disabled = 'disabled';
        textAfterTypo.value = state.text;
        textAfterTypo.classList.add('hc-red');
        textAfterTypo.addEventListener('input', (e) => {
          e.preventDefault();
          if (textTypo.value !== textAfterTypo.value) {
            textAfterTypo.classList.remove('hc-red');
          } else {
            textAfterTypo.classList.add('hc-red');
          }
        })
        btn.addEventListener('click', closePopup);
      }
      document.body.appendChild(popup);
      popupClose.addEventListener('click', closePopup);
      popupBody.addEventListener('click', closePopup);
      popupContent.addEventListener('click', event => event.stopPropagation());
      iframe.onerror = function () {
        console.log("Что-то пошло не так... ");
      };
    }
  })
});
