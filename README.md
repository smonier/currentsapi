# News Currents API

This Jahia jContent V8 module is designed to retrieve News from the Currents API 
(https://currentsapi.services/en)
---
### API KEY
<YOUR_INSTALL_DIR>/digital-factory-data/karaf/etc/org.jahia.se.modules.currentsapi.taglibs.CurrentsApiService.cfg
```
currentsApiKey=<YOUR_API_KEY>
```
## Query Parameters
The components presents 2 choices based on the API:

1. Latest News in a specific language
2. A Search with the following criterias:
 - searchKeyword
 - category 
 - region 
 - language

### Screenshots
Isotope List
![picture](./src/main/resources/images/readme/currentsAPIscreenshot.png)

Vertical Carousel
![picture](./src/main/resources/images/readme/currentsAPIscreenshot2.png)
