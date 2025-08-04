import "./ProfilePage.scss";
import Header from "../components/Header";
import Footer from "../components/Footer";
import AppContext from "../context/AppContext";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const ProfilePage = () => {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState({ name: "", email: "" });
  const { setIsLoggedIn } = useContext(AppContext);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const response = await fetch("http://localhost:8080/auth/user", {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) throw new Error("사용자 정보를 불러오지 못했습니다");

        const data = await response.json();
        setUserInfo({ name: data.name, email: data.email });
      } catch (error) {
        console.error("Error fetching user info:", error);
      }
    };

    fetchUserInfo();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("accessToken"); // 토큰 제거
    setIsLoggedIn(false);
    navigate("/");
  };

  const handleDeleteAccount = async () => {
    const confirmed = window.confirm("Sure you want to delete your Account?");
    if (!confirmed) return;

    const password = prompt("Re-Enter your password:");
    if (!password) return;

    try {
      const token = localStorage.getItem("accessToken");
      const response = await fetch("http://localhost:8080/users/me", {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ password }),
      });

      if (response.ok) {
        alert("회원 탈퇴가 완료되었습니다.");
        localStorage.removeItem("accessToken");
        setIsLoggedIn(false);
        navigate("/");
      } else {
        const errorData = await response.json();
        alert(errorData.error || "회원 탈퇴에 실패했습니다.");
      }
    } catch (error) {
      console.error("회원 탈퇴 중 오류:", error);
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  return (
    <div className="profile-page">
      <Header />

      <main className="profile-page__main">
        <div className="profile-page__top">
          <h2>My Account</h2>
          <button className="profile-page__logout" onClick={handleLogout}>Log Out</button>
        </div>

        <div className="profile-page__form">
          <label>Name</label>
          <input type="text" value={userInfo.name} readOnly />

          <label>Email</label>
          <input type="email" value={userInfo.email} readOnly />

          <button className="profile-page__save-button">Edit Profile</button>

          <div className="profile-page__warning">
            <p className="profile-page__delete-link" onClick={handleDeleteAccount}>
              Delete Account
            </p>
            <p className="profile-page__warning-description">
              When you delete your account, all drives and related data within the workspaces you participated in will be permanently removed.
            </p>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default ProfilePage;