export interface User {
    name: string;
    gender: string;
    email: string;
}

interface UsersByCountry{
    name: string;
    users: User[];
}

export interface GetAllUsersByCountryReponse {
    countries: UsersByCountry[];
}