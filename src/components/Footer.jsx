import "./Footer.scss";
import logoImage from '../assets/logo.png';
import discordImage from '../assets/discord_logo.png';
import notionImage from '../assets/notion_logo.png';
import githubImage from '../assets/github_logo.png';
import gachonImage from '../assets/gachon_logo.png';
import sejongImage from '../assets/sejong_logo.png';
import academicImage from '../assets/academic_logo.png';


const Footer = () => {
  return (
    <footer className="footer">
      <div className="left">
        <div href="/" className="logo">
          <img src={logoImage} alt="logo" className="logo__img" />
          <span className="logo__text">spAIk</span>
        </div>
        <p>spAIk’s mission is to empower individuals to imporve their presentation
          skills through AI-driven analysis and personalized feedback.</p>
      </div>
      <div className="right">
        <div className="section">
          <strong>About</strong>
          <div className="icons">
            <a href="https://discord.gg/NQrfjpsx" target="_blank" rel="noopener noreferrer">
              <img src={discordImage} alt="Discord" className="icon-image" />
            </a>
            <a href="https://www.notion.so/spAIk-225b71be02228087b862e7b533505be9" target="_blank" rel="noopener noreferrer">
              <img src={notionImage} alt="Notion" className="icon-image" />
            </a>
            <a href="https://github.com/spAIk-GS" target="_blank" rel="noopener noreferrer">
              <img src={githubImage} alt="Github" className="icon-image" />
            </a>
          </div>
        </div>
        <div className="section">
          <strong>Support</strong>
          <div className="icons">
            <img src={gachonImage} alt="Gachon" className="icon-image" />
            <img src={sejongImage} alt="Sejong" className="icon-image" />
            <img src={academicImage} alt="Academic" className="icon-image2" />
          </div>
        </div>
      </div>
      <div className="copyright">@2025 spAIk, Team.</div>
    </footer>
  );
};

export default Footer;
