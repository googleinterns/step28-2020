var googleTrends = require('/home/anerishah/step-donation/step28-2020/Donation-platform/src/main/webapp/frontend/node_modules/google-trends-api/lib/google-trends-api.min.js');

/* ******************* Autocomplete **************************/

googleTrends.autoComplete({keyword: 'Back to school'})
.then((res) => {
  console.log('this is res', res);
})
.catch((err) => {
  console.log('got the error', err);
  console.log('error message', err.message);
  console.log('request body',  err.requestBody);
});