import "./MainPage.scss";
import mainImage from "../assets/mainPageImage.png";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { motion } from "framer-motion";


const MainPage = () => {
  return (
    <div className="main-page">
      <Header />

      <main className="main-page__main">
        <div className="main-page__illustration">
          <motion.img
            src={mainImage}
            alt="Illustration"
            initial={{ x: -100, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            transition={{ duration: 1 }}
          />
        </div>
        <motion.div
          className="main-page__text-block"
          initial={{ x: 100, opacity: 0 }}
          animate={{ x: 0, opacity: 1 }}
          transition={{ duration: 1, delay: 0.3 }}
        >
          <p className="main-page__quote">
            “ Ability to present with confidence<br />
            is the cornerstone of your<br />
            team’s achievements. ”
          </p>
          <button className="main-page__cta">Get Started</button>
        </motion.div>
      </main>

      <Footer />

    </div>
  );
};

export default MainPage;
