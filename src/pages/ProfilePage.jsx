import "./ProfilePage.scss";
import Header from "../components/Header";
import Footer from "../components/Footer";

const ProfilePage = () => {
  return (
    <div className="profile-page">
      <Header />
      <main className="profile-page__main">
        <div className="profile-page__columns">
          <div className="profile-page__left">
          <div className="profile-page__avatar-placeholder" />
        </div>

        <div className="profile-page__right">
          {/* 임시 */}
          <h2>hwangmouse</h2>
          <div className="profile-page__buttons">
            <button className="edit-btn">Edit Profile</button>
            <button className="logout-btn">Logout</button>
          </div>
          <div className="profile-page__field">
            <label>Email</label>
            <input type="text" value="0327jieun@naver.com" readOnly />
          </div>
          <div className="profile-page__field">
            <label>Password</label>
            <input type="password" value="********" readOnly />
          </div>

          {/* <div className="profile-page__posts">
            <div className="posts-header">
              <h3>My Post (4)</h3>
              <span className="see-more">See More ▶</span>
            </div>
            <div className="post-box">
              <div className="post-content">시선 처리 잘하는 법 공유좀요.</div>
              <div className="post-date">2025.07.11</div>
            </div>
            <div className="post-box">
              <div className="post-content">실제로 발표 대본 다 암기해서 하는 사람이 있냐...</div>
              <div className="post-date">2025.07.09</div>
            </div>
          </div> */}
        </div>
      </div>
      </main>
      <Footer />
    </div>
  );
};

export default ProfilePage;