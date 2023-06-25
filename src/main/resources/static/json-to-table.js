window.addEventListener('load', () => {
    let urlInputElement = document.querySelector('#url-input');
    function fetchAndProcessData() {
        document.querySelector('#json-table').innerHTML = '';
        document.querySelector('#error-container').textContent = '';
        document.querySelector('.loader').classList.add('show');
        let url = urlInputElement.value;
        fetch(url).then(response => response.json())
            .then((data) => processData(data))
            .catch((error) => {
                console.error('Error:', error);
                renderError(error);
            }).finally(() => {
                document.querySelector('.loader').classList.remove('show');
                document.querySelector('.loader').classList.add('hide');
            });
    }

    document.querySelector('#fetch-data-btn').addEventListener('click', () => {
        fetchAndProcessData();
    });
    urlInputElement.addEventListener('keyup', (e) => {
        if (e.code === 'Enter') {
            fetchAndProcessData();
        }
    });
});

function processData(data = {}) {
    const serialNumKey = 'sno';
    let tableData = [];
    let tableHeaderKeys = [serialNumKey];
    if (data instanceof Array) {
        tableData = data;
    } else if (typeof data === 'object') {
        tableData.push(data);
    }
    if (tableData.length > 0) {
        tableHeaderKeys.push(...Object.keys(tableData[0]));
    }
    // Add a serial number property to all table row objects
    tableData.forEach((data, index) => {
        data['sno'] = index + 1;
    });
    renderTable(tableHeaderKeys, tableData);
}

function renderTable(tableHeaderKeys = [], tableData = []) {
    // Build table header
    let tableHeader = document.createElement('thead');
    let headerRow = document.createElement('tr');
    tableHeaderKeys.forEach(key => {
        let headerCell = document.createElement('th');
        headerCell.textContent = key;
        headerRow.appendChild(headerCell)
    });
    tableHeader.appendChild(headerRow);

    // Build table body
    let tableBody = document.createElement('tbody');
    tableData.forEach(obj => {
        let row = document.createElement('tr');
        tableHeaderKeys.forEach(key => {
            let cell = document.createElement('td');
            cell.textContent = obj[key];
            row.appendChild(cell);
        });
        tableBody.appendChild(row);
    });

    // Add table header and body to page
    let tableElement = document.querySelector('#json-table');
    tableElement.innerHTML = '';
    tableElement.append(tableHeader, tableBody);
}

function renderError(error = '') {
    document.querySelector('#error-container').textContent = error;
}