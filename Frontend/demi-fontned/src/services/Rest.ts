import axios from 'axios';
import {GetAllUsersByCountryReponse} from './models/GetAllUsersByCountryReponse';

export class RestService {
    public static getAllUsers = async () => {
        const response = await axios.get('http://127.0.0.1:8080/api/users');

        if (!response || response.status !== 200) { return null; }

        return response.data as GetAllUsersByCountryReponse;
    }
}