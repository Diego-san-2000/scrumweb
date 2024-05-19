function enviar(URL, jsonAEnviar, metodo){
    fetch(URL, {
        method: metodo,
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonAEnviar
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Network response was not ok.');
    })
    .then(data => {
        console.log('Response from server:', data);
        // Perform actions with the response data as needed
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
}

function enviarBorrar(URL){
    fetch(URL, {
        method: "DELETE"
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}