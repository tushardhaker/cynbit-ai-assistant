document.addEventListener('DOMContentLoaded', () => {
    const launcher = document.getElementById('chatLauncher');
    const modal = document.getElementById('chatModal');
    const closeBtn = document.getElementById('closeChat');
    const sendBtn = document.getElementById('sendBtn');
    const chatInput = document.getElementById('chatInput');
    const chatBody = document.getElementById('chatBody');

    launcher.addEventListener('click', () => {
        modal.classList.remove('hidden');
        launcher.classList.add('hidden');
    });

    closeBtn.addEventListener('click', () => {
        modal.classList.add('hidden');
        launcher.classList.remove('hidden');
    });

    function appendMessage(text, sender) {
        const msg = document.createElement('div');
        msg.className = `msg msg-${sender}`;
        msg.textContent = text;
        chatBody.appendChild(msg);
        chatBody.scrollTop = chatBody.scrollHeight;
        return msg;
    }

    async function sendMessage(text) {
        if (!text || !text.trim()) return;

        appendMessage(text, 'user');
        chatInput.value = '';

        try {
            const res = await fetch('/api/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: text })
            });
            const data = await res.json();

            appendMessage(data.reply, 'bot');

            if (data.action === 'PROMPT_LEAD') {
                showLeadForm();
            } else if (data.suggestedActions && data.suggestedActions.length > 0) {
                renderChips(data.suggestedActions);
            }
        } catch (err) {
            appendMessage('Unable to reach assistant backend.', 'bot');
        }
    }

    function renderChips(chips) {
        const chipsContainer = document.createElement('div');
        chipsContainer.className = 'chips';
        chips.forEach(action => {
            const chip = document.createElement('span');
            chip.className = 'chip';
            chip.textContent = action;
            chip.addEventListener('click', () => {
                chipsContainer.remove();
                sendMessage(action);
            });
            chipsContainer.appendChild(chip);
        });
        chatBody.appendChild(chipsContainer);
        chatBody.scrollTop = chatBody.scrollHeight;
    }

    function showLeadForm() {
        const leadBox = document.createElement('div');
        leadBox.className = 'lead-box';
        leadBox.innerHTML = `
            <input type="text" id="leadName" placeholder="Your Full Name" required />
            <input type="email" id="leadEmail" placeholder="Work Email" required />
            <button id="submitLead">Send Scope & Quote Request</button>
        `;
        chatBody.appendChild(leadBox);
        chatBody.scrollTop = chatBody.scrollHeight;

        leadBox.querySelector('#submitLead').addEventListener('click', async () => {
            const name = leadBox.querySelector('#leadName').value;
            const email = leadBox.querySelector('#leadEmail').value;
            if (!email) return;

            leadBox.remove();
            appendMessage(`Submitted inquiry for ${email}`, 'user');

            const res = await fetch('/api/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ leadName: name, leadEmail: email })
            });
            const data = await res.json();
            appendMessage(data.reply, 'bot');
        });
    }

    sendBtn.addEventListener('click', () => sendMessage(chatInput.value));
    chatInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') sendMessage(chatInput.value);
    });
});