import "./ProfilePage.scss";
import Header from "../components/Header";
import Footer from "../components/Footer";
import AppContext from "../context/AppContext";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const ProfilePage = () => {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState({ name: "", email: "" });
  const [editable, setEditable] = useState(false);
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [isOAuthUser, setIsOAuthUser] = useState(false); // 소셜 로그인 유저 여부
  const { setIsLoggedIn } = useContext(AppContext);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const response = await fetch("http://localhost:8080/users/me", {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) throw new Error("사용자 정보를 불러오지 못했습니다");

        const data = await response.json();
        setUserInfo({ name: data.name, email: data.email });
        if (data.oauthProvider) {
          setIsOAuthUser(true); // 소셜 로그인 유저인 경우 이메일 수정 금지
        }
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

  const handleEditProfile = async () => {
    if (!editable) {
      setEditable(true); // 편집 모드 켜기
      return;
    }

    if (!currentPassword) {
      alert("현재 비밀번호를 입력해주세요.");
      return;
    }

    try {
      const token = localStorage.getItem("accessToken");
      const response = await fetch("http://localhost:8080/users/me", {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: userInfo.name,
          email: userInfo.email,
          password: newPassword,
          currentPassword: currentPassword,
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        alert(errorData.error || "회원 정보 수정에 실패했습니다.");
        return;
      }

      const updatedUser = await response.json();
      alert("회원 정보가 성공적으로 수정되었습니다.");
      setUserInfo({ name: updatedUser.user.name, email: updatedUser.user.email });
      setEditable(false);
      setCurrentPassword("");
      setNewPassword("");
    } catch (error) {
      console.error("회원 정보 수정 오류:", error);
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
          <input
            type="text"
            value={userInfo.name}
            onChange={(e) => setUserInfo({ ...userInfo, name: e.target.value })}
            readOnly={!editable}
          />

          <label>Email</label>
          <input
            type="email"
            value={userInfo.email}
            onChange={(e) => setUserInfo({ ...userInfo, email: e.target.value })}
            readOnly={!editable || isOAuthUser}
          />

          {editable && (
            <>
              <label>Current Password</label>
              <input
                type="password"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
              />

              <label>New Password (optional)</label>
              <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
              />
            </>
          )}

          <button className="profile-page__save-button" onClick={handleEditProfile}>
            {editable ? "Save Changes" : "Edit Profile"}
          </button>

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