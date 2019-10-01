import { sendCorrection } from './index';

const getHeaderElement = (i18n) => {
    const header = document.createElement('div');
    header.className = 'HexletCorrection-header';
    header.innerText = i18n.header;
    return header;
};

const getSnippetLabelElement = (i18n) => {
    const label = document.createElement('div');
    label.className = 'HexletCorrection-label';
    label.innerText = i18n.snippetLabel;
    return label;
};

const getSnippetElement = () => {
    const snippetElement = document.createElement('div');
    const selection = document.createElement('span');
    const before = document.createElement('span');
    const after = document.createElement('span');
    snippetElement.className = 'HexletCorrection-snippet';
    snippetElement.id = 'HexletCorrection-snippet';
    selection.className = 'HexletCorrection-selection';
    selection.id = 'HexletCorrection-selection';
    before.id = 'HexletCorrection-beforeSelection';
    after.id = 'HexletCorrection-afterSelection';
    snippetElement.append(before);
    snippetElement.append(selection);
    snippetElement.append(after);
    return snippetElement;
};

const getCommentInputElement = () => {
    const commentInput = document.createElement('textarea');
    commentInput.id = 'HexletCorrection-comment';
    commentInput.className = 'HexletCorrection-comment';
    commentInput.rows = 5;
    commentInput.cols = 100;
    return commentInput;
};

const getQuestionLabelElement = (i18n) => {
    const label = document.createElement('div');
    label.className = 'HexletCorrection-label';
    label.innerText = i18n.questionLabel;
    return label;
};

const getButtonGroupElement = (i18n) => {
    const buttonGroup = document.createElement('div');
    const buttonSend = document.createElement('button');
    const buttonCancel = document.createElement('button');

    buttonSend.disabled = true;

    buttonSend.className = 'HexletCorrection-send-btn HexletCorrection-btn';
    buttonCancel.className = 'HexletCorrection-cancel-btn HexletCorrection-btn';

    buttonSend.innerText = i18n.sendBtn;
    buttonCancel.innerText = i18n.closeBtn;

    buttonGroup.appendChild(buttonSend);
    buttonGroup.appendChild(buttonCancel);

    return buttonGroup;
};

const createModalForm = (i18n) => {
    const headerElement = getHeaderElement(i18n);
    const snippetLabelElement = getSnippetLabelElement(i18n);
    const snippetElement = getSnippetElement();
    const commentInputElement = getCommentInputElement();
    const questionLabelElement = getQuestionLabelElement(i18n);
    const buttonGroupElement = getButtonGroupElement(i18n);

    const modalContainer = document.createElement('div');
    modalContainer.className = 'HexletCorrection-container';

    modalContainer.appendChild(headerElement);
    modalContainer.appendChild(snippetLabelElement);
    modalContainer.appendChild(snippetElement);
    modalContainer.appendChild(commentInputElement);
    modalContainer.appendChild(questionLabelElement);
    modalContainer.appendChild(buttonGroupElement);

    const modal = document.createElement('div');
    modal.id = 'HexletCorrection';
    modal.appendChild(modalContainer);

    return modal;
};

const addStyle = () => {
    let style = document.getElementById('HexletCorrection-style');
    if (!style) {
        style = document.createElement('style');
        style.id = 'HexletCorrection-style';
        style.innerText = "#HexletCorrection{position:fixed;top:0;z-index:999;width:100%;display:flex;flex-direction:column;align-items:center;font-family:sans-serif}.HexletCorrection-container{max-width:60%;margin:2rem auto;padding:2rem 3rem;box-shadow:0 0 15px #555;border-radius:15px;background-color:#fff;display:flex;flex-direction:column}.HexletCorrection-header{font-weight:700;font-size:150%;margin-bottom:1rem}.HexletCorrection-label{color:#666;margin-bottom:.5rem}.HexletCorrection-snippet{background-color:#f3f3f3;margin-bottom:1rem;padding:.5rem}.HexletCorrection-selection{color:red;text-decoration:underline;font-weight:700;margin:0 .5rem}.HexletCorrection-comment{width:100%;border:solid 1px #ccc;margin-bottom:.5rem}.HexletCorrection-btn{height:2rem;font-size:100%;border:0;border-radius:.5rem}.HexletCorrection-send-btn{background:#add8e6}.HexletCorrection-cancel-btn{background:#d3d3d3}";
        document.head.appendChild(style);
    }
};

const addModal = (i18n) => {
    const modal = createModalForm(i18n);
    modal.hidden = true;
    document.body.appendChild(modal);
};

const modalBinding = () => {
    const sendBtn = document.getElementsByClassName('HexletCorrection-send-btn')[0];
    sendBtn.addEventListener('click', sendCorrection);

    document.getElementsByClassName('HexletCorrection-cancel-btn')[0]
        .addEventListener('click', closeModal);

    const commentField = document.getElementById('HexletCorrection-comment');
    commentField.onkeyup = () => sendBtn.disabled = !commentField.value;
};

const initModal = (typo) => {
    const selectionElement = document.getElementById('HexletCorrection-selection');
    const beforeSelectionElement = document.getElementById('HexletCorrection-beforeSelection');
    const afterSelectionElement = document.getElementById('HexletCorrection-afterSelection');

    selectionElement.innerText = typo.selection;
    beforeSelectionElement.innerText = typo.before;
    afterSelectionElement.innerText = typo.after;
};

export const init = (i18n) => {
    addStyle();
    addModal(i18n);
    modalBinding();
};

export const showModal = (typo) => {
    initModal(typo);
    document.getElementById('HexletCorrection').hidden = false;
};

export const closeModal = () => {
    document.getElementById('HexletCorrection').hidden = true;
    document.getElementById('HexletCorrection-comment').value = '';
    document.getElementsByClassName('HexletCorrection-send-btn')[0].disabled = true;
};
