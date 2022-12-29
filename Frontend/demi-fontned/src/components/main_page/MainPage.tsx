import React, { useEffect, useMemo, useState } from 'react';

import "./MainPage.css";
import {RestService} from "../../services/Rest";
import Flex from "../flex/Flex";
import UsersTable from '../users-table/UsersTable';

import Combobox from "react-widgets/Combobox";
import "react-widgets/styles.css";
import { GetAllUsersByCountryReponse } from '../../services/models/GetAllUsersByCountryReponse';

interface IMainPageProps {}

const MainPage = (props: IMainPageProps): JSX.Element => {
    const defaultComboboxValue = 'Unknown';

    const [selectedCountry, setSelectedCountry] = useState<string>(defaultComboboxValue);
    const [usersByCountry, setUsersByCountry] = useState<GetAllUsersByCountryReponse | null>(null);

    const getAndSetUsersByCountry = async () => {
        const usersByCountry = await RestService.getAllUsers();
        setUsersByCountry(usersByCountry);
    }

    useEffect(() => {
        (async () => {
            await getAndSetUsersByCountry();
        })();
    }, []);

    const countries = useMemo(() => {
        return usersByCountry?.countries.map(c => c.name);
    }, [usersByCountry]);

    const usersForCountry = useMemo(() => {
        const usersForSelectedCountry = usersByCountry?.countries.filter(c => c.name === selectedCountry);
        return usersForSelectedCountry && usersForSelectedCountry.length && usersForSelectedCountry[0].users ? 
            usersForSelectedCountry[0].users : [];    
    }, [usersByCountry, selectedCountry]);

    const onRefreshButtonClick = async () => {
        await getAndSetUsersByCountry();
        setSelectedCountry(defaultComboboxValue)
    }

    return (
        <div className='main-page'>
            <Flex direction='col' classNames="align-center">
                <Flex direction='row'>
                    <Flex direction='col' style={{marginRight: '10px'}}>
                        <Combobox
                            value={selectedCountry}
                            data={countries}
                            onChange={(value) => { setSelectedCountry(value); }}
                        />
                    </Flex>
                    <Flex direction='col' classNames='justify-center'>
                        <button onClick={async () => { await onRefreshButtonClick() ;} }>Refresh</button>
                    </Flex>
                </Flex>

                <Flex direction='row' style={{marginTop: '20px'}}>
                    {usersForCountry.length !== 0 && (<UsersTable data={usersForCountry}/>)}
                    {usersForCountry.length === 0 && (<p> Select a country to display the users for it</p>)}

                </Flex>
            </Flex>
           
           
        </div>
    );
}

export default MainPage;