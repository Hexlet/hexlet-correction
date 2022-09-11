const modal = document.querySelector('#typoModal');

const typoField = modal.querySelector('#typoText');

const correctedTextField = modal.querySelector('#correctedText');

const commentTextField = modal.querySelector('#commentText');

const closeBtn = modal.querySelector('#closeBtn');

const cancelBtn = modal.querySelector('#cancelBtn');

const sendTypoBtn = modal.querySelector('#sendTypoBtn');

const defaultText = 'Выделите текст с ошибкой';

const state = {
  typoText: '',
  correctedText: '',
  commentText: '',
  formStatus: '',
};

const processingPressedKeys = (func, ...codes) => {
  const pressedKeys = new Set();

  document.addEventListener('keydown', (event) => {
    pressedKeys.add(event.code);

    for (let code of codes) {
      if (!pressedKeys.has(code)) {
        return;
      }
    }

    pressedKeys.clear();

    func();
  });

  document.addEventListener('keyup', (event) => {
    pressedKeys.delete(event.code);
  });
};

const getTextWithTypo = () => {
  const select = window.getSelection();
  state.typoText = select.type === 'Caret' ? defaultText : select.toString();
};

const createTypoObject = () => {
  const pageUrl = document.location.href;
  const reporterName = 'reporterName';
  const reporterComment = state.commentText;
  const textBeforeTypo = state.typoText;
  const textTypo = 'textTypo';
  const textAfterTypo = state.correctedText;

  return {
    pageUrl,
    reporterName,
    reporterComment,
    textBeforeTypo,
    textTypo,
    textAfterTypo,
  };
};

const openTypoModal = (modalId) => {
  const modal = new bootstrap.Modal(modalId);
  getTextWithTypo();
  typoField.textContent = state.typoText;
  modal.show();
};

const sendTypo = (data) => {
  fetch('http://localhost:8080/api/workspaces/test-workspace/typos', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: 'Token e3844052-4bb9-4e54-99b7-b8705365d521',
    },
    body: JSON.stringify(data),
  });
};

const processingForm = (formStatus) => {
  switch (formStatus) {
    case 'filled': {
      modal.classList.remove('show');
      modal.setAttribute('style', 'display: none');
      document.querySelector('body').removeAttribute('style', 'class');
      document.querySelector('body').setAttribute('style', 'padding-top: 4.5rem');
      correctedTextField.value = '';
      sendTypoBtn.setAttribute('disabled', 'disabled');
      const data = createTypoObject(modal);
      sendTypo(data);
      break;
    }
    case 'fillingError': {
      typoField.classList.add('border', 'border-3', 'border-danger');
      correctedTextField.classList.add('border', 'border-3', 'border-danger');
      break;
    }
    default:
      throw new Error('Unexpected from status');
  }
};

correctedTextField.addEventListener('input', (event) => {
  typoField.classList.remove('border', 'border-3', 'border-danger');
  correctedTextField.classList.remove('border', 'border-3', 'border-danger');

  state.correctedText = event.target.value;
  sendTypoBtn.removeAttribute('disabled');
});

commentTextField.addEventListener('input', (event) => {
  state.commentText = event.target.value;
});

closeBtn.addEventListener('click', (event) => {
  typoField.classList.remove('border', 'border-3', 'border-danger');
  correctedTextField.classList.remove('border', 'border-3', 'border-danger');

  correctedTextField.value = '';
  sendTypoBtn.setAttribute('disabled', 'disabled');
});

cancelBtn.addEventListener('click', (event) => {
  typoField.classList.remove('border', 'border-3', 'border-danger');
  correctedTextField.classList.remove('border', 'border-3', 'border-danger');
  
  correctedTextField.value = '';
  sendTypoBtn.setAttribute('disabled', 'disabled');
});

sendTypoBtn.addEventListener('click', (event) => {
  event.preventDefault();

  if (state.typoText !== defaultText && state.correctedText !== '') {
    state.formStatus = 'filled';
  } else {
    state.formStatus = 'fillingError';
  }

  processingForm(state.formStatus);
});

processingPressedKeys(() => openTypoModal(modal), 'ControlLeft', 'Enter');
