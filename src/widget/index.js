const generateModalStyles = () => {
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
    overflow: auto;
    outline: 0;
    background-color: rgba(0, 0, 0, 0.5);
  }

  #hexlet-correction-modal_modal-content {
    background-color: #fefefe;
    margin: 10rem auto;
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
    border-radius: 5px;
    margin-right: 15px;
    padding: 0;
  }

  #hexlet-correction-modal_ReportTypo-cancel {
    height: 35px;
    width: 120px;
    background-color: #f8f9fa;
    color: #212529;
    border-color: #dee2e6;
    border: none;
    border-radius: 5px;
    padding: 0;
  }

  #hexlet-correction-modal_ReportTypo-submit:hover {
    background-color: #1e7cd6;
  }

  #hexlet-correction-modal_ReportTypo-cancel:hover {
    background-color: #e2e6ea;
  }
  `;
  document.head.append(modalStyleEl);

  return modalStyleEl;
};

const handleBodyScroll = (modalIsOpen) => {
  if (modalIsOpen) {
    document.body.style.overflow = 'hidden';
  } else {
    document.body.style.overflow = 'auto';
  }
};

const generateModal = (state) => {
  generateModalStyles();

  const modalEl = document.createElement('div');
  modalEl.id = 'hexlet-correction-modal_modal';
  modalEl.style.display = state.modalShown ? 'block' : 'none';

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

  const inputName = document.createElement('input');
  inputName.type = 'text';
  inputName.placeholder = 'Введите свое имя или email';
  inputName.id = 'hexlet-correction-modal_ReportTypo-name';
  inputName.value = state.options.userName === null ? '' : state.options.userName;

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
  submitBtn.id = 'hexlet-correction-modal_ReportTypo-submit';
  submitBtn.textContent = 'Отправить';

  const cancelBtn = document.createElement('button');
  cancelBtn.type = 'button';
  cancelBtn.id = 'hexlet-correction-modal_ReportTypo-cancel';
  cancelBtn.textContent = 'Отмена';

  divButtons.append(submitBtn, cancelBtn);
  divTypoReporter.append(divHeader, divFirstLabel, selectedTextEl, inputName, commentEl, divSecondLabel, divButtons);

  const body = document.querySelector('body');
  body.append(modalEl);

  return {
    modalEl,
    selectedTextEl,
    commentEl,
    inputName,
    submitBtn,
    cancelBtn,
  };
};

const resetModalState = (state) => {
  state.modalShown = false;
  state.data.reporterComment = '';
  state.data.textBeforeTypo = '';
  state.data.textTypo = '';
  state.data.textAfterTypo = '';
};

const renderModal = (elements, state) => {
  if (state.modalShown) {
    elements.modalEl.style.display = 'block';
    const { textBeforeTypo, textTypo, textAfterTypo } = state.data;
    elements.selectedTextEl.innerHTML = `${textBeforeTypo}<u id="hexlet-correction-modal_ReportTypo-highlight">${textTypo}</u>${textAfterTypo}`;
    elements.inputName.value = state.data.reporterName !== '' ? state.data.reporterName : state.options.userName;
    elements.commentEl.value = state.data.reporterComment;
    elements.commentEl.focus();
    return;
  }

  elements.modalEl.style.display = 'none';
  elements.selectedTextEl.innerHTML = '';
  elements.commentEl.value = '';
};

const sendData = (elements, state) => async (event) => {
  event.preventDefault();
  const { value } = elements.inputName;
  state.data.reporterName = value === '' ? 'Anonymous' : value;
  state.data.reporterComment = elements.commentEl.value;
  try {
    await fetch(`${state.options.workSpaceUrl}/api/workspaces/${state.options.workSpaceId}/typos`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Basic ${state.options.authorizationToken}`,
      },
      body: JSON.stringify(state.data),
    });
    resetModalState(state);
  } catch (error) {
    throw new Error('Произошла ошибка:', error);
  }
};

const view = (elements, state) => {
  const watch = (state, callback) => new Proxy(state, {
    set(target, prop, value) {
      const prevValue = target[prop];
      const result = Reflect.set(target, prop, value);
      callback(prop, value, prevValue);
      return result;
    },
  });

  const watchedState = watch(state, (path) => {
    switch (path) {
      case 'modalShown':
        renderModal(elements, state);
        handleBodyScroll(state.modalShown);
        break;
      default:
        break;
    }
  });

  return watchedState;
}

const handleTypoReporter = (options) => {
  if (!options || !options.authorizationToken && !options.workSpaceId) {
    throw new Error('Для работы модуля требуется указать workSpaceId и authorizationToken');
  }

  const initialState = {
    modalShown: false,
    options: {
      workSpaceUrl: 'https://hexlet-correction.herokuapp.com/api/workspaces',
      userName: null,
      ...options,
    },
    data: {
      pageUrl: '',
      reporterName: '',
      reporterComment: '',
      textBeforeTypo: '',
      textTypo: '',
      textAfterTypo: '',
    },
  };

  const elements = generateModal(initialState);
  const state = view(elements, initialState);

  document.addEventListener('keydown', (event) => {
    const selection = window.getSelection();
    if (selection.isCollapsed) {
      return;
    }
    if (event.ctrlKey && event.key === 'Enter') {
      state.data.pageUrl = window.location.href;

      const { anchorNode } = selection;
      const { anchorOffset } = selection;
      const { focusOffset } = selection;
      const maxLength = 50;
      const end = Math.min(focusOffset + maxLength, anchorNode.length);
      const start = Math.max(anchorOffset - maxLength, 0);

      state.data.textTypo = selection.toString();
      state.data.textBeforeTypo = anchorNode.textContent.substring(start, anchorOffset);
      state.data.textAfterTypo = anchorNode.substringData(focusOffset, end - focusOffset);
      state.modalShown = true;
    }
  });

  elements.submitBtn.addEventListener('click', sendData(elements, state));
  elements.cancelBtn.addEventListener('click', () => resetModalState(state));

  elements.modalEl.addEventListener('click', (event) => {
    if (event.target === elements.modalEl) {
      resetModalState(state);
    }
  });

  elements.modalEl.addEventListener('keydown', (event) => {
    if (state.modalShown && event.key === 'Escape') {
      resetModalState(state);
    }
  });
};
