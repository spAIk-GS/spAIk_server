import "./MainPage.scss";
import mainImage from "../assets/temp_mainPageImage.png";
import Header from "../components/Header";
import Footer from "../components/Footer";

const MainPage = () => {
  return (
    <div className="main-page">
      <Header />

      <main className="main-page__main">
        <div className="main-page__illustration">
          <img src={mainImage} alt="Illustration" />
        </div>
        <div className="main-page__text-block">
          <p className="main-page__quote">
            “ Ability to present with confidence<br />
            is the cornerstone of your team’s achievements. ”
          </p>
          <button className="main-page__cta">Get Started</button>
        </div>
      </main>

      <Footer />

    </div>
  );
};

export default MainPage;
