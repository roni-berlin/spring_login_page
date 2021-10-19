import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";
import Loader from "react-loader-spinner";
import { useCookies } from "react-cookie";
import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";
import api from "../api";

interface user {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}
const HomePage = () => {
  const [cookies, setCookie, removeCookie] = useCookies([
    "accessToken",
    "refreshToken",
    "userId",
  ]);
  const [user, setUser] = useState<user>();
  const history = useHistory();

  useEffect(() => {
    const getUser = async () => {
      let accessToken = await cookies.accessToken;
      if (!accessToken) {
        await api.refreshToken();
      }
      accessToken = await cookies.accessToken;
      if (!accessToken) {
        history.push("/login");
      } else {
        const connectedUser = await api.validate();
        setUser(connectedUser);
      }
    };
    getUser();
  }, [cookies.accessToken, cookies.refreshToken, cookies.userId, history]);

  const logOut = () => {
    removeCookie("accessToken");
    removeCookie("refreshToken");
    removeCookie("userId");
    history.push({ pathname: "/login" });
  };
  if (!user) {
    return (
      <div className="d-flex justify-content-md-center align-items-center vh-100">
        <Loader type="CradleLoader" height={300} width={300} />
      </div>
    );
  }
  return (
    <div>
      {user && (
        <>
          <h1 className="d-flex justify-content-md-center align-items-center my-5 display-1">
            {`Hello ${user.firstName} ${user.lastName}`}
          </h1>
          <div className="d-flex justify-content-center">
            <button className="btn btn-secondary" onClick={logOut}>
              log out
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default HomePage;
