import { init, showModal, closeModal } from './ModalForm';

const translations = {
    'ru': {
        header: 'Сообщите об ошибке на странице',
        snippetLabel: 'Ошибка содержится в следующем тексте:',
        questionLabel: 'Отправить сообщение об ошибке редактору сайта?',
        sendBtn: 'Отправить',
        closeBtn: 'Отмена'
    },
    'en': {
        header: 'Report a mistake on the page',
        snippetLabel: 'There is a mistake in the following text:',
        questionLabel: 'Do you want to send a notice to a webmaster?',
        sendBtn: 'Send',
        closeBtn: 'Cancel'
    }
};

const locale = window['hc'].q[0][1] || 'en';
const i18n = translations[locale];

const state = {
    account: {id: window['hc'].q[0][0]},
    typo: {
        before: '',
        selection: '',
        after: ''
    },
    comment: '',
    pageURL: window.location.toString(),
    valid: false
};

const hasSelection = () => {
    const selection = window.getSelection();
    return selection && selection.toString().trim();
};

const parseTypo = () => {
    const selection = window.getSelection();
    const selectionString = selection.toString();

    const minify = (str) => {
        return str.replace(/[\r\n\s]+/g, ' ').trim();
    };

    if (selectionString) {
        const contentAroundLen = 50;
        const selectionRange = selection.getRangeAt(0);
        const startRange = document.createRange();
        const endRange = document.createRange();

        startRange.setStartBefore(selectionRange.startContainer.ownerDocument.body);
        startRange.setEnd(selectionRange.startContainer, selectionRange.startOffset);

        endRange.setStart(selectionRange.endContainer, selectionRange.endOffset);
        endRange.setEndAfter(selectionRange.endContainer.ownerDocument.body);

        const startSnippet = startRange.toString().substr(-contentAroundLen);
        const endSnippet = endRange.toString().substr(0, contentAroundLen);

        return {
            before: minify(startSnippet),
            selection: minify(selectionString),
            after: minify(endSnippet)
        };
    }
};

export const sendCorrection = () => {
    const correction = {
        account: state.account,
        pageURL: state.pageURL,
        comment: document.getElementById('HexletCorrection-comment').value,
        highlightText: state.typo.selection,
        beforeHighlight: state.typo.before,
        afterHighlight: state.typo.after
    };

    const host = 'https://hexlet-correction.herokuapp.com';
    const path = '/api/v1/corrections';
    const request = new XMLHttpRequest();
    request.open('POST', host + path, true);
    request.setRequestHeader('Content-Type', 'application/json');
    request.onload = () => {console.log('Correction has been sent.')};
    request.onerror = () => {console.log('Error sending correction.')};
    request.send(JSON.stringify(correction));

    closeModal();
};

document.onkeyup = (e) => {
    if (e.ctrlKey && e.key === 'Enter' && hasSelection()) {
        state.typo = parseTypo();
        showModal(state.typo);
    }
};

init(i18n);
