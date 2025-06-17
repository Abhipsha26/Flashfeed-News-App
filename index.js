const functions = require('firebase-functions');
const admin = require('firebase-admin');
const axios = require('axios');

admin.initializeApp();

// GNews API configuration
const GNEWS_API_KEY = "3e9a2ad4b3431efa2160de3216076846";
const GNEWS_URL = `https://gnews.io/api/v4/top-headlines?country=in&lang=en&apikey=${GNEWS_API_KEY}`;

exports.updateNews = functions.pubsub
    .schedule('every 30 minutes')
    .onRun(async (context) => {
        try {
            // Fetch news from GNews
            const response = await axios.get(GNEWS_URL);
            const newsData = response.data;

            if (newsData.articles) {
                const db = admin.database();
                const newsRef = db.ref('News');

                // Clear existing news
                await newsRef.remove();

                // Add new articles
                const articles = newsData.articles.slice(0, 25);
                const updates = {};

                articles.forEach((article, index) => {
                    updates[index + 1] = {
                        newslink: article.url,
                        imagelink: article.image || '',
                        head: article.title,
                        title: article.title,
                        desc: article.description || ''
                    };
                });

                await newsRef.update(updates);
                console.log(`Successfully updated ${articles.length} news articles`);
            }
        } catch (error) {
            console.error('Error updating news:', error);
        }
    }); 