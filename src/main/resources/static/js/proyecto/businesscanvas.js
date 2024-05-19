function guardarBusinessCanvas(idBusinessCanvas){
    let keyPartners = document.getElementById("keyPartners").value;
    let keyActivities = document.getElementById("keyActivities").value;
    let keyResources = document.getElementById("keyResources").value;
    let valuePropositions = document.getElementById("valuePropositions").value;
    let customerRelationships = document.getElementById("customerRelationships").value;
    let channels = document.getElementById("channels").value;
    let customersSegments = document.getElementById("customersSegments").value;
    let costStructure = document.getElementById("costStructure").value;
    let revenueStreams = document.getElementById("revenueStreams").value;

    let data = {
        "keyPartners": keyPartners,
        "keyActivities": keyActivities,
        "keyResources": keyResources,
        "valuePropositions": valuePropositions,
        "customerRelationships": customerRelationships,
        "channels": channels,
        "customersSegments": customersSegments,
        "costStructure": costStructure,
        "revenueStreams": revenueStreams
    };
    
    console.log(data);
    enviar('/api/businesscanvas/'+idBusinessCanvas, JSON.stringify(data), 'PUT');
}