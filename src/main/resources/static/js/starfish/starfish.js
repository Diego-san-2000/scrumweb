function anadir() {
    let texto = document.getElementById("texto").value;
    let selectElement = document.getElementById("tipo");
    let selectedOption = selectElement.options[selectElement.selectedIndex].value;

    let lista = document.getElementById(selectedOption);

    // Create a new <li> element
    let liElement = document.createElement("li");

    // Create a new delete button
    let deleteButton = document.createElement("i");
    deleteButton.className = "bi bi-trash";
    deleteButton.onclick = function () {
        deleteItem(this);
    };

    // Append the delete button to the <li> element
    liElement.appendChild(deleteButton);

    // Set the text content of the new <li> element
    liElement.appendChild(document.createTextNode(" " + texto));

    // Append the new <li> element to the <ul> element
    lista.appendChild(liElement);
}
function deleteItem(button) {
    let li = button.parentNode;
    let ul = li.parentNode;
    ul.removeChild(li);
  }

function guardar(idStarfish){
    const result = {};

    // Get all lists with class "lista"
    const listContainers = document.querySelectorAll('.lista');

    listContainers.forEach(container => {
        const ulId = container.querySelector('ul').id;
        const listItems = Array.from(container.querySelectorAll('li')).map(li => li.textContent);
        result[ulId] = listItems;
    });

    const jsonString = JSON.stringify(result, null, 2);
    console.log(jsonString);
    const URL = "/api/starfish/"+ idStarfish;
    enviar(URL, jsonString, "PUT");
}