# News Currents API



This Jahia jContent V8 module is designed to retreive News from the Currents API 
(https://currentsapi.services/en)
---
### API KEY
You need to add to your jahia.properties the API key that you'll get from Currents API (600 request a day for free)
jahia.modules.publicnewsapi.attr.currentsApi.key=<YOUR_API_KEY>
## Query Parameters
The components presents 2 choices based on the API:

1. Latest News in a specific language
2. A Search with the following criterias:
 - searchKeyword
 - category 
 - Search news after the given date
 - region 
 - language

![picture](./src/main/resources/images/readme/currentsAPIscreenshot.png)
