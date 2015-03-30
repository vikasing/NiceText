/**
 *
 */
package com.vikasing.nicetext;


//import crow.utils.WebUtils;


import crow.global.G;

/**
 * @author vikasing
 */
public class TestNiceText {

    /**
     * @param args
     */
    public static void main(String[] args) {
        G.init();
        /*WebUtils htmlUtils = new WebUtils();
		Set<String> urlSet = htmlUtils.getLinksFromWebPage("https://news.ycombinator.com/news");
		for (String url : urlSet) {
			System.out.println(url+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			String text = htmlHelper.getText(url).get();
			System.out.println(text);
		}*/

        String[] urls = new String[]{
                "http://www.npr.org/2015/03/27/395593337/twist-of-fate-an-accident-brings-beautiful-symmetry-to-two-lives",
                "http://www.oneindia.com/sports/cricket/bangladesh-captain-speaks-on-aleem-dar-s-no-ball-error-at-world-cup-1689394.html",
                "http://www.independent.co.uk/news/world/middle-east/isis-in-libya-muammar-gaddafis-soldiers-are-back-in-the-country-and-fighting-under-the-black-flag-of-the-islamic-state-10111964.html",
                "http://www.chinadaily.com.cn/business/2015-03/20/content_19862174.htm",
                "http://www.vox.com/2015/3/16/8225977/dick-vitale-talking",
                "http://economictimes.indiatimes.com/news/politics-and-nation/supreme-court-extends-interim-bail-to-teesta-setalvad-and-husband-javed-anand/articleshow/46628427.cms",
                "http://www.theaustralian.com.au/business/latest/rba-awaits-data-before-more-easing/story-e6frg90f-1227265988415",
                "http://www.sfchronicle.com/crime/article/Man-shot-to-death-by-Napa-police-6134255.php",
                "http://www.cio.com.au/whitepaper/372445/tintri-vmstore-application-aware-storage/?type=section&arg=51236&location=rhs_featured_whitepaper",
                "http://www.newsday.com/business/msg-president-and-ceo-resigns-tad-smith-takes-same-roles-at-sotheby-s-1.10067686#disqus_thread",
                "http://www.theaustralian.com.au/business/news/asic-puts-payday-lenders-on-notice/story-e6frg906-1227265934107",
                "http://www.njherald.com/story/28526788/10-things-to-know-for-today",
                "http://www.jpost.com/International/US-Senate-leader-Obama-on-cusp-of-very-bad-deal-with-Iran-393972",
                "http://www.ndtv.com/karnataka-news/karnataka-governor-walks-off-during-national-anthem-745652",
                "http://www.aninews.in/newsdetail2/story203457/will-bounce-back-after-two-three-years-congress.html",
                "http://edition.cnn.com/2015/03/10/world/afghanistan-violence/index.html",
                "http://economictimes.indiatimes.com/news/politics-and-nation/membership-drive-bjp-turns-to-mps-mlas-for-final-push-to-make-it-worlds-largest-party/articleshow/46449753.cms",
                "http://www.independent.co.uk/sport/cricket/ecb-proposes-end-of-the-fiveday-cricket-test-match-10071057.html",
                "http://www.independent.co.uk/news/business/news/private-equity-driver-gears-up-for-auto-trader-flotation-10074117.html",
                "http://www.thehindu.com/news/national/andhra-pradesh/tap-aquaculture-potential-to-full/article6943174.ece?homepage=true",
                "http://www.denverpost.com/ci_27602128/charles-koch-working-business-book-scheduled-october",
                "http://www.irishtimes.com/news/world/secretive-bilderberg-group-sets-sights-on-michael-o-leary-1.2119343",
                "http://www.independent.co.uk/news/uk/crime/claudia-lawrence-father-of-missing-chef-says-it-is-dreadful-people-may-have-lied-to-police--as-officers-carry-out-search-of-alleyway-10069547.html",
                "http://www.theaustralian.com.au/national-affairs/health/two-children-tested-for-ebola-in-melbourne-hospital/story-fn59nokw-1227239685887",
                "http://www.thestar.com/business/2015/02/19/oil-slump-could-dip-inflation-into-the-negative-boc.html",
                "http://www.reuters.com/article/2015/02/18/us-health-obesity-idUSKBN0LM2E320150218",
                "http://www.ndtv.com/diaspora/us-lawmaker-tulsi-gabbard-to-marry-in-april-in-vedic-ceremony-740759?pfrom=home-diaspora",
                "http://www.thehindu.com/news/cities/Delhi/kejriwal-seeks-services-of-sanjeev-chaturvedi/article6905600.ece?ref=topnavwidget&utm_source=topnavdd&utm_medium=topnavdropdownwidget&utm_campaign=topnavdropdown",
                "http://www.deccanherald.com/content/458482/karnataka-man-seen-cctv-footage.html",
        };
        for (String url : urls) {
            String[] t = NTFactory.getNiceText(url).split("\n");
            StringBuilder txtB = new StringBuilder();
            for (String s : t) {
                s = s.trim();
                if (s.charAt(s.length() - 1) == '.') {
                    txtB.append(s).append(" ");
                } else {
                    txtB.append(s).append(". ");
                }
            }
            System.out.println(txtB.toString());
            System.out.println("==================================");
        }

		/*
		NGramExtracter nExtracter = new NGramExtracter();
		Map<String, SortedSet<Entry<String, Integer>>> nGramMap = nExtracter.extract(text);
		SortedSet<Entry<String, Integer>> bigrams = nGramMap.get("bi");
		for (Entry<String, Integer> entry : bigrams) {
			if (entry.getValue()>1) {
				System.out.println(entry.getKey() +" "+entry.getValue());
			}
		}
		SortedSet<Entry<String, Integer>> trigrams = nGramMap.get("tri");
		for (Entry<String, Integer> entry : trigrams) {
			if (entry.getValue()>1) {
				System.out.println(entry.getKey() +" "+entry.getValue());
			}
		}
		
		SortedSet<Entry<String, Integer>> monograms = nGramMap.get("mono");
		for (Entry<String, Integer> entry : monograms) {
			if (entry.getValue()>1) {
				System.out.println(entry.getKey() +" "+entry.getValue());
			}
		}*/
    }

}
