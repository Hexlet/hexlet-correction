const handleTypoReporter = (options) => {
  if (!options || !options.authorizationToken && !options.workSpaceId) {
    throw new Error('Для работы модуля требуется указать workSpaceId и authorizationToken');
  }
  const { workSpaceUrl = 'https://hexlet-correction.herokuapp.com/api/workspaces', userName = null, authorizationToken, workSpaceId } = options;
  const state = {
    modalShown: false,
  };
  const renderModal = () => {
    const divModal = document.createElement('div');
    divModal.id = 'hexlet-correction-modal_modal';
    divModal.style.display = 'block';

    const divContent = document.createElement('div');
    divContent.id = 'hexlet-correction-modal_modal-content';
    divModal.append(divContent);

    const divTypoReporter = document.createElement('div');
    divTypoReporter.id = 'hexlet-correction-modal_ReportTypo';
    divContent.append(divTypoReporter);

    const divHeader = document.createElement('div');
    divHeader.id = 'hexlet-correction-modal_ReportTypo-header';
    divHeader.textContent = 'Сообщите об ошибке на странице';

    const divFirstLabel = document.createElement('div');
    divFirstLabel.classList.add('hexlet-correction-modal_ReportTypo-label');
    divFirstLabel.textContent = 'Ошибка содержится в следующем тексте:';

    const divMessage = document.createElement('div');
    divMessage.id = 'hexlet-correction-modal_ReportTypo-message';

    const inputName = document.createElement('input');
    inputName.type = 'text';
    inputName.placeholder = 'Введите свое имя или email';
    inputName.value = userName === null ? '' : userName;
    inputName.id = 'hexlet-correction-modal_ReportTypo-name';

    const textareaComment = document.createElement('textarea');
    textareaComment.id = 'hexlet-correction-modal_ReportTypo-comment';
    textareaComment.placeholder = 'Опишите ошибку';

    const divSecondLabel = document.createElement('div');
    divSecondLabel.classList.add('hexlet-correction-modal_ReportTypo-label');
    const strong = document.createElement('strong');
    strong.id = 'hexlet-correction-modal_question';
    strong.textContent = 'Отправить сообщение об ошибке редактору сайта?';
    divSecondLabel.append(strong);

    const divButtons = document.createElement('div');
    divButtons.style.textAlign = 'right';

    const submitButton = document.createElement('button');
    submitButton.type = 'button';
    submitButton.id = 'hexlet-correction-modal_ReportTypo-submit'
    submitButton.textContent = 'Отправить';

    const cancelButton = document.createElement('button');
    cancelButton.type = 'button';
    cancelButton.id = 'hexlet-correction-modal_ReportTypo-cancel'
    cancelButton.textContent = 'Отмена';

    divButtons.append(submitButton, cancelButton);
    divTypoReporter.append(divHeader, divFirstLabel, divMessage, inputName, textareaComment, divSecondLabel, divButtons);
    const body = document.querySelector('body');
    body.append(divModal);

    state.modalShown = true;

    const style = document.createElement('style');
    style.textContent = `
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
    document.head.append(style);
  };

  const data = {
    pageUrl: null,
    reporterName: null,
    reporterComment: null,
    textBeforeTypo: null,
    textTypo: null,
    textAfterTypo: null,
  };

  let sendDataHandler = null;

  document.addEventListener('keydown', (event) => {
    const selection = window.getSelection();
    if (selection.isCollapsed) {
      return;
    }
    if (event.ctrlKey && event.key === 'Enter') {
      if (state.modalShown === false) {
        renderModal();
      }
      const modal = document.getElementById('hexlet-correction-modal_modal');
      const selectedText = document.getElementById('hexlet-correction-modal_ReportTypo-message');
      const commentField = document.getElementById('hexlet-correction-modal_ReportTypo-comment');
      const submitButton = document.getElementById('hexlet-correction-modal_ReportTypo-submit');
      const cancelBtn = document.getElementById('hexlet-correction-modal_ReportTypo-cancel');
      const name = document.getElementById('hexlet-correction-modal_ReportTypo-name');

      const closeModal = () => {
        modal.style.display = 'none';
        commentField.value = '';
        name.value = '';
        submitButton.removeEventListener('click', sendDataHandler);
        cancelBtn.removeEventListener('click', sendDataHandler);
      };


      const sendData = async (event) => {
        event.preventDefault();
        data.pageUrl = window.location.href;
        data.reporterName = name.value === '' ? 'Anonymous' : name.value;
        data.reporterComment = commentField.value;
        try {
          await fetch(`${workSpaceUrl}/${workSpaceId}/typos`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Basic ${authorizationToken}`
            },
            body: JSON.stringify(data)
          });
          closeModal();
        } catch (error) {
          throw new Error('Произошла ошибка:', error);
        }
      };

      modal.style.display = 'block';
      const selectionText = selection.toString().trim();
      const anchorNode = selection.anchorNode;
      const anchorOffset = selection.anchorOffset;
      const focusOffset = selection.focusOffset;
      const maxLength = 50;
      const end = Math.min(focusOffset + maxLength, anchorNode.length);
      const start = Math.max(anchorOffset - maxLength, 0);
      const textBeforeSelection = anchorNode.textContent.substring(start, anchorOffset);
      const textAfterSelection = anchorNode.substringData(focusOffset, end - focusOffset);

      selectedText.innerHTML = `${textBeforeSelection}<u id="hexlet-correction-modal_ReportTypo-highlight">${selectionText}</u>${textAfterSelection}`;
      commentField.focus();

      data.textTypo = selectionText;
      data.textBeforeTypo = textBeforeSelection;
      data.textAfterTypo = textAfterSelection;

      if (sendDataHandler) {
        submitButton.removeEventListener('click', sendDataHandler);
        cancelBtn.removeEventListener('click', sendDataHandler);
      }

      sendDataHandler = sendData;

      cancelBtn.addEventListener('click', closeModal);
      submitButton.addEventListener('click', sendData);
    }
  });
};
