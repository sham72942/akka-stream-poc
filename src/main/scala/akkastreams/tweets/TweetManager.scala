package akkastreams.tweets

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}

class TweetManager(tweets: Source[Tweet, NotUsed])(implicit val system: ActorSystem) {
  def matchingTweetAuthors(hashtag: Hashtag): Unit = {
    val authors: Source[Author, NotUsed] =
      tweets.filter(_.hashtags.contains(hashtag)).map(_.author)

    authors
      .runWith(Sink.foreach(println))
      .onComplete(_ => system.terminate())(system.dispatcher)
  }

}
