import * as dotenv from "dotenv";
import axios from "axios";
import Swal from "sweetalert2";
dotenv.config({ path: "../.env" });
const url: string = `${process.env.REACT_APP_SERVER_HOST as string}:${
  process.env.REACT_APP_SERVER_PORT as string
}`;
const USERS = "/users";
const SESSIONS = "/sessions";

interface loginData {
  email: string;
  password: string;
}

interface registerData extends loginData {
  firstname: string;
  lastName: string;
}

// eslint-disable-next-line import/no-anonymous-default-export
export default {
  async login(loginInfo: loginData) {
    try {
      await axios.get(`${url}${USERS}/sign-in`, {
        params: loginInfo,
        withCredentials: true,
      });
    } catch (e: any) {
      this.showErrorModal(e);
    }
  },

  async addNewUser(registerInfo: registerData) {
    try {
      await axios.post(`${url}${USERS}/sign-up`, registerInfo);
    } catch (e: any) {
      this.showErrorModal(e);
    }
  },
  async register(registerInfo: registerData) {
    try {
      this.addNewUser(registerInfo);
      this.login({
        email: registerInfo.email,
        password: registerInfo.password,
      });
    } catch (e: any) {
      this.showErrorModal(e);
    }
  },
  async refreshToken() {
    try {
      await axios.get(`${url}${SESSIONS}/refresh-token`, {
        withCredentials: true,
      });
    } catch (e: any) {
      console.log({ e });
    }
  },
  async validate() {
    try {
      const res = await axios.get(`${url}${USERS}/validate`, {
        withCredentials: true,
      });
      return res.data;
    } catch (e) {
      console.log(e);
    }
  },
  showErrorModal(e: any) {
    Swal.fire({
      title: "oops...",
      text: e.response?.data?.message || `cannot register\n Error:${e.message}`,
      icon: e.response?.status > 400 ? "error" : "warning",
    });
  },
};
