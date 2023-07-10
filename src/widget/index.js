const renderModalStyleEl = () => {
  const modalStyleEl = document.createElement('style');
  modalStyleEl.textContent = `
  #hexlet-correction-modal_modal {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 1050;
    display: none;
    width: 100%;
    height: 100%;
    overflow: hidden;
    outline: 0;
    background-color: rgba(0, 0, 0, 0.5);
  }

  #hexlet-correction-modal_modal-content {
    background-color: #fefefe;
    margin: 15% auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
    max-width: 700px;
  }

  #hexlet-correction-modal_ReportTypo-header {
    font-weight: 700;
    font-size: 150%;
    margin-bottom: 1rem;
    width: 100%;
    flex-shrink: 0;
  }

  .hexlet-correction-modal_ReportTypo-label {
    margin-bottom: 0.5rem;
    display: flex;
    flex-direction: column;
    flex-shrink: 0;
  }

  #hexlet-correction-modal_ReportTypo-message {
    width: 100%;
    padding: 5px 5px;
    margin-bottom: 15px;
    border: 1px solid #f3f3f3;
    background: #f3f3f3;
    overflow-y: auto;
  }

  #hexlet-correction-modal_ReportTypo-comment {
    display: block;
    width: 100%;
    height: 100px;
    margin-bottom: 1rem;
    border: 1px solid #ccc;
    flex-shrink: 0;
    padding-right: 8px;
  }

  #hexlet-correction-modal_ReportTypo-name {
    display: block;
    width: 100%;
    height: 40px;
    margin-bottom: 1rem;
    border: 1px solid #ccc;
    flex-shrink: 0;
    padding: 5px;
  }

  #hexlet-correction-modal_ReportTypo-highlight {
    text-decoration: underline;
    color: black;
    font-weight: 700;
    margin: 0 10px;
  }

  #hexlet-correction-modal_question {
    margin-bottom: 8px;
  }

  #hexlet-correction-modal_ReportTypo-submit {
    height: 35px;
    width: 120px;
    background-color: #0d6efd;
    color: #fff;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    margin-right: 15px;
  }

  #hexlet-correction-modal_ReportTypo-cancel {
    height: 35px;
    width: 120px;
    background-color: #f8f9fa;
    color: #212529;
    border-color: #dee2e6;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
  }

  #hexlet-correction-modal_ReportTypo-submit:hover {
    background-color: #1e7cd6;
  }

  #hexlet-correction-modal_ReportTypo-cancel:hover {
    background-color: #e2e6ea;
  }
  `
  document.head.append(modalStyleEl);

  return modalStyleEl;
}

const renderModal = (state) => {
  const modalEl = document.createElement('div');
  modalEl.id = 'hexlet-correction-modal_modal';
  modalEl.style.display = 'block';

  const divContent = document.createElement('div');
  divContent.id = 'hexlet-correction-modal_modal-content';
  modalEl.append(divContent);

  const divTypoReporter = document.createElement('div');
  divTypoReporter.id = 'hexlet-correction-modal_ReportTypo';
  divContent.append(divTypoReporter);

  const divHeader = document.createElement('div');
  divHeader.id = 'hexlet-correction-modal_ReportTypo-header';
  divHeader.textContent = 'Сообщите об ошибке на странице';

  const divFirstLabel = document.createElement('div');
  divFirstLabel.classList.add('hexlet-correction-modal_ReportTypo-label');
  divFirstLabel.textContent = 'Ошибка содержится в следующем тексте:';

  const selectedTextEl = document.createElement('div');
  selectedTextEl.id = 'hexlet-correction-modal_ReportTypo-message';

  const { textBeforeTypo, textTypo, textAfterTypo } = state.data;
  selectedTextEl.innerHTML = `${textBeforeTypo}<u id="hexlet-correction-modal_ReportTypo-highlight">${textTypo}</u>${textAfterTypo}`;

  const inputName = document.createElement('input');
  inputName.type = 'text';
  inputName.placeholder = 'Введите свое имя или email';
  inputName.value = state.options.userName === null ? '' : state.options.userName;
  inputName.id = 'hexlet-correction-modal_ReportTypo-name';

  const commentEl = document.createElement('textarea');
  commentEl.id = 'hexlet-correction-modal_ReportTypo-comment';
  commentEl.placeholder = 'Опишите ошибку';

  const divSecondLabel = document.createElement('div');
  divSecondLabel.classList.add('hexlet-correction-modal_ReportTypo-label');
  const strong = document.createElement('strong');
  strong.id = 'hexlet-correction-modal_question';
  strong.textContent = 'Отправить сообщение об ошибке редактору сайта?';
  divSecondLabel.append(strong);

  const divButtons = document.createElement('div');
  divButtons.style.textAlign = 'right';

  const submitBtn = document.createElement('button');
  submitBtn.type = 'button';
  submitBtn.id = 'hexlet-correction-modal_ReportTypo-submit'
  submitBtn.textContent = 'Отправить';

  const cancelBtn = document.createElement('button');
  cancelBtn.type = 'button';
  cancelBtn.id = 'hexlet-correction-modal_ReportTypo-cancel'
  cancelBtn.textContent = 'Отмена';

  divButtons.append(submitBtn, cancelBtn);
  divTypoReporter.append(divHeader, divFirstLabel, selectedTextEl, inputName, commentEl, divSecondLabel, divButtons);
  const body = document.querySelector('body');
  body.append(modalEl);

  return {
    modalEl,
    selectedTextEl,
    commentEl,
    submitBtn,
    cancelBtn,
    inputName,
    modalStyleEl: renderModalStyleEl(),
  };
};

const runModal = (dataForModal) => {
  const textTypo = dataForModal.selection.toString().trim();
  const anchorNode = dataForModal.selection.anchorNode;
  const anchorOffset = dataForModal.selection.anchorOffset;
  const focusOffset = dataForModal.selection.focusOffset;
  const maxLength = 50;
  const end = Math.min(focusOffset + maxLength, anchorNode.length);
  const start = Math.max(anchorOffset - maxLength, 0);
  const textBeforeTypo = anchorNode.textContent.substring(start, anchorOffset);
  const textAfterTypo = anchorNode.substringData(focusOffset, end - focusOffset);

  const state = {
    options: {
      workSpaceUrl: 'https://hexlet-correction.herokuapp.com/api/workspaces',
      userName: null,
      ...dataForModal.options,
    },
    data: {
      pageUrl: dataForModal.pageUrl,
      reporterName: null,
      reporterComment: null,
      textBeforeTypo,
      textTypo,
      textAfterTypo,
    },
  };

  const elements = renderModal(state);

  const removeModal = () => {
    elements.modalEl.remove();
    elements.modalStyleEl.remove();
  };

  const sendData = async (event) => {
    event.preventDefault();
    state.data.pageUrl = window.location.href;
    const value = elements.inputName.value;
    state.data.reporterName = value === '' ? 'Anonymous' : value;
    state.data.reporterComment = elements.commentEl.value;
    try {
      await fetch(`${state.options.workSpaceUrl}${state.options.workSpaceId}/typos`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Basic ${state.options.authorizationToken}`
        },
        body: JSON.stringify(state.data)
      });
      removeModal();
    } catch (error) {
      console.error(error);
      throw new Error('Произошла ошибка:', error);
    }
  };

  elements.commentEl.focus();
  elements.cancelBtn.addEventListener('click', removeModal);
  elements.submitBtn.addEventListener('click', sendData);

  const handlerСlickPastModal = (event) => {
    if (event.target === elements.modalEl) {
      removeModal();
      document.removeEventListener('click', handlerСlickPastModal);
    }
  };
  document.addEventListener('click', handlerСlickPastModal);
};

const handleTypoReporter = (options) => {
  if (!options || !options.authorizationToken && !options.workSpaceId) {
    throw new Error('Для работы модуля требуется указать workSpaceId и authorizationToken');
  }

  document.addEventListener('keydown', (event) => {
    const selection = window.getSelection();
    if (selection.isCollapsed) {
      return;
    }
    if (event.ctrlKey && event.key === 'Enter') {
      const dataForModal = {
        selection,
        pageUrl: window.location.href,
        options,
      }
      runModal(dataForModal);
    }
  });
};
