function guardarServiceBlueprint(id){
    let data = {
        'a11':document.getElementById('11').value,
        'a12':document.getElementById('12').value,
        'a13':document.getElementById('13').value,
        'a14':document.getElementById('14').value,
        'a15':document.getElementById('15').value,
        'a21':document.getElementById('21').value,
        'a22':document.getElementById('22').value,
        'a23':document.getElementById('23').value,
        'a24':document.getElementById('24').value,
        'a25':document.getElementById('25').value,
        'a31':document.getElementById('31').value,
        'a32':document.getElementById('32').value,
        'a33':document.getElementById('33').value,
        'a34':document.getElementById('34').value,
        'a35':document.getElementById('35').value,
        'a41':document.getElementById('41').value,
        'a42':document.getElementById('42').value,
        'a43':document.getElementById('43').value,
        'a44':document.getElementById('44').value,
        'a45':document.getElementById('45').value,
        'a51':document.getElementById('51').value,
        'a52':document.getElementById('52').value,
        'a53':document.getElementById('53').value,
        'a54':document.getElementById('54').value,
        'a55':document.getElementById('55').value,
        'a61':document.getElementById('61').value,
        'a62':document.getElementById('62').value,
        'a63':document.getElementById('63').value,
        'a64':document.getElementById('64').value,
        'a65':document.getElementById('65').value
    };

    console.log(data);
    enviar('/api/serviceblueprint/'+id, JSON.stringify(data), 'PUT');
}
