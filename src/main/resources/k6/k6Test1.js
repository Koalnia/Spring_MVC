import http from 'k6/http';
import { check, sleep } from 'k6';
import { parseHTML } from 'k6/html';

export default function () {
    const BASE_URL = 'http://localhost:8080';

    // 1. Pobranie strony logowania w celu uzyskania tokenu CSRF
    let loginPageResponse = http.get(`${BASE_URL}/login`);

    // Sprawdzenie czy strona logowania została poprawnie załadowana
    check(loginPageResponse, {
        'Pomyślne załadowanie strony logowania': (r) => r.status === 200
    });

    // Parsowanie tokenu anty-CSRF ze strony logowania
    let doc = parseHTML(loginPageResponse.body);
    let token = doc.find('input[name="_csrf"]').attr('value');
    console.log('Token anty-CSRF: ' + token);


    // 2. Przygotowanie danych logowania
    const loginPayload = {
        username: 'admin1@pl',
        password: 'Hasloha11!',
        _csrf: token,  // Dodanie tokenu CSRF do payload
      };

    // Przygotowanie nagłówków z tokenem CSRF
    let params = {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token,
        },
        redirects: 0 // Zapobiega automatycznemu przekierowaniu, aby móc przeanalizować nagłówki odpowiedzi
    };

    // 3. Wykonanie żądania logowania
    let loginResponse = http.post(`${BASE_URL}/login`, loginPayload, params);

    // Sprawdzenie odpowiedzi logowania
    check(loginResponse, {
        'Logowanie zakończone sukcesem (302 redirect)': (r) => r.status === 302,
        'Posiada ciasteczko uwierzytelnienia': (r) => r.cookies['JSESSIONID'] !== undefined
    });

    // 4. Wydobycie i wyświetlenie ciasteczka uwierzytelniającego
    let authCookie = null;
    if (loginResponse.cookies['JSESSIONID']) {
        console.log('Pobrano ciasteczko JSESSIONID:');
        console.log(JSON.stringify(loginResponse.cookies['JSESSIONID'], null, 2));

        // Zapisanie ciasteczka do użycia w innych testach
        authCookie = loginResponse.cookies['JSESSIONID'];
    }

    // 5. Przygotowanie nagłówków dla kolejnych żądań
    let headers = {
        'X-CSRF-TOKEN': token
    };

    // Przygotowanie ciasteczek dla kolejnych żądań
    let cookies = {
        'JSESSIONID': authCookie ? authCookie.value : ''
    };

    // 6. Wykonanie żądania do stron z listą ogłoszeń oraz użytkowników z użyciem pobranego ciasteczka
    let advertisementPageResponse = http.get(`${BASE_URL}/advertisement/index`, {
        headers: headers,
        cookies: cookies
    });

    check(advertisementPageResponse, {
        'Pomyślne załadowanie strony ogłoszeń': (r) => r.status === 200
    });
    //console.log('Advertisement Response:', JSON.stringify(advertisementPageResponse));

    let usersPageResponse = http.get(`${BASE_URL}/user/index`, {
        headers: headers,
        cookies: cookies
    });

    check(usersPageResponse, {
        'Pomyślne załadowanie strony użytkowników': (r) => r.status === 200
    });
    //console.log('Users Response:', JSON.stringify(usersPageResponse));

    // 7. Wykonanie żądania Update dla ogłoszenia o ID 15
    //Przejście na strone edycji ogłoszenia o id 15
    let editPageResponse = http.get(`${BASE_URL}/advertisement/update/15`, {
        headers: headers,
        cookies: cookies
    });
    check(editPageResponse, {
        'Pomyślne załadowanie strony edycji ogłoszenia nr 15': (r) => r.status === 200
    });
    //console.log('editPage:', JSON.stringify(editPageResponse));

    // Parsowanie nowego tokenu anty-CSRF ze strony logowania
    doc = parseHTML(editPageResponse.body);
    token = doc.find('input[name="_csrf"]').attr('value');
    console.log('Nowy token anty-CSRF: ' + token);
    headers = {
        'X-CSRF-TOKEN': token
    };
    function generateRandomPrice() {
        let randomNumber = Math.floor(Math.random() * 9999) + 1; // Losowa liczba od 1 do 9999
        return `${randomNumber} zł`; // Łączenie liczby z "zł"
    }
    let formData = {
        'Title': 'Oddam regały na książki',
        'Description': 'Dwa takie same regały z IKEA, 5 półek, wymiary 200x80x30 cm',
        'Price': generateRandomPrice(),
        'Duration': 'Brak',
        '__RequestVerificationToken': token
    };
    // Wykonanie żądania post w celu edycji danych ogłoszenia o ID 15
    const editResponse = http.post(`${BASE_URL}/advertisement/update/15`, formData,{
        headers: headers,
        cookies: cookies
    });

    check(editResponse, {
        'Pomyślna edycja ogłoszenia nr 15': (r) => r.status === 200
    });
    //console.log('Advertisement Update Response Status:', editResponse.status);

    sleep(1);
}