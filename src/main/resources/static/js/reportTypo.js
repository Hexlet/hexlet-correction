const iframe = document.getElementsByTagName('iframe')[0];

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

const showModal = (iframe) => {
	iframe.style.display = "block";

	iframe.style.height = document.body.clientHeight + 600 + 'px';

	const defaultText = 'Выделите текст с ошибкой';
	
	const innerDoc = iframe.contentDocument || iframe.contentWindow.document;
   
	const body = innerDoc.body;
	
	const form = body.querySelector('form');

	const typoField = body.querySelector('#typoText'); 

	const correctedTextField = body.querySelector('#correctedText');

	const commentTextField = body.querySelector('#commentText');

	const closeBtn = body.querySelector('#closeBtn');

	const cancelBtn = body.querySelector('#cancelBtn');

	const sendTypoBtn = body.querySelector('#sendTypoBtn');

	const state = {
		typoText: '',
		correctedText: '',
		commentText: '',
		formStatus: '',
	};

	const getTextWithTypo = () => {
		const select = window.getSelection();
		state.typoText = select.type === 'Caret' ? defaultText : select.toString();
	};

	getTextWithTypo();

	typoField.textContent = state.typoText;

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

	const sendTypo = (data) => {
		fetch('http://localhost:8080/api/workspaces/test/typos', {
		  method: 'POST',
		  headers: {
			'Content-Type': 'application/json',
			Authorization: 'Token d2e21f09-50b0-4f8b-bc88-393c24fc3cbb',
		  },
		  body: JSON.stringify(data),
		});
	  };

	const processingForm = (formStatus) => {
		switch (formStatus) {
			case 'filled': {
				sendTypoBtn.setAttribute('disabled', 'disabled');
				iframe.style.height = '0px';
				iframe.style.display = "none";
				const data = createTypoObject();
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
		iframe.style.height = '0px';
		iframe.style.display = "none";
		state.typoText = '';
		typoField.classList.remove('border', 'border-3', 'border-danger');
		correctedTextField.classList.remove('border', 'border-3', 'border-danger');
	
		correctedTextField.value = '';
		sendTypoBtn.setAttribute('disabled', 'disabled');
	});

	cancelBtn.addEventListener('click', (event) => {
		iframe.style.height = '0px';
		iframe.style.display = "none";
		state.typoText = '';
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

};

processingPressedKeys(() => showModal(iframe), 'ControlLeft', 'Enter');
