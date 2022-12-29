import { User } from "../../services/models/GetAllUsersByCountryReponse";

interface IUsersTableProps {
    data: User[];
};

const UsersTable = ({data}: IUsersTableProps): JSX.Element => {
    return (
        <table>
            <thead>
                <tr>
                    <th style={{textAlign: "left"}}>Name</th>
                    <th style={{textAlign: "left"}}>Email</th>
                    <th style={{textAlign: "left"}}>Gender</th>
                </tr>
            </thead>

            <tbody>
                {data.map((user: User, index: number) => (
                    <tr key={index}>
                        <td>{user.name}</td>
                        <td>{user.email}</td>
                        <td>{user.gender}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

export default UsersTable;