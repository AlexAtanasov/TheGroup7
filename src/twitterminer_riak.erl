-module(twitterminer_riak).

-export([counter_reset/1, count/1, bucketRecurser/1, get_riak_hostport/1,getCount/1, bucketSaver/1, bucketRequest/1,
	getRating/1,stringDate/0,getOlddata/1]).

-record(hostport, {host, port}).

% This file contains example code that connects to Twitter and saves tweets to Riak.
% It would benefit from refactoring it together with twitterminer_source.erl.

keyfind(Key, L) ->
  {Key, V} = lists:keyfind(Key, 1, L),
  V.

%% @doc Get Twitter account keys from a configuration file.
get_riak_hostport(Name) ->
  {ok, Nodes} = application:get_env(twitterminer, riak_nodes),
  {Name, Keys} = lists:keyfind(Name, 1, Nodes),
  #hostport{host=keyfind(host, Keys),
            port=keyfind(port, Keys)}.

%% @doc This example will download a sample of tweets and print it.

%Returns a string representation of date in the required format for the bucketRecurser()

stringDate() -> {YYYY,MM,DD} = date(),
Date = lists:flatten(io_lib:format("\"date\": \"~p",[YYYY])),
Datem = Date ++ lists:flatten(io_lib:format("-~p",[MM])),
Datem ++ lists:flatten(io_lib:format("-~p\"",[DD])).

%%Saves the counter and other information to the data bucket, then resets the counter. It recurses every 24 hours until encountering an error or explicitly stopped.
%%Argument is the Bucket with the counters 
bucketSaver(Bucket) -> {_,Keylist} = bucketRequest(Bucket),
bucketRecurser(Keylist),
resetRecurser(Keylist),
receive
	_ -> ok
after 86400000 -> bucketSaver(Bucket)
end. 

%Official bucket Hashtags_Data
%Goes through a list and makes put requests for all keys, we store The old data of the bucket, the counter, current imdb rating and current date is
%put inside the request and stored on the database in a format. A bit slow but performance is not an issue for this component. 
%input should be a list of movies without hashtags

bucketRecurser([Key|List]) -> 
io:format("~s", [Key]),
Keystone= io_lib:format("~s",[Key]),
CounterKey =io_lib:format("#~s",[Keystone]),
Imdbrating= getRating(Keystone),
Olddata= getOlddata(Keystone),
Counter = getStringCount(CounterKey),
ProtoCommand = io_lib:format(
"curl -v -XPUT -H \"Content-Type: application/json\"  http://129.16.155.12:10018/buckets/Counters_Data/keys/~s -d\"",[Key]),%inserts new data + old data
Date = stringDate(),
Data = Olddata++[123]++[10]++Date++[44]++[10]++Counter++[44]++[10]++Imdbrating++[10]++[125]++[44],
Command = ProtoCommand ++ Data ++[34],
%io:format("~s", [Command]),
os:cmd(Command),
bucketRecurser(List);

bucketRecurser([])->ok.

%Resets the Counter for all keys in a list, Doesn't work for inputs with ' etc..
resetRecurser([Key|List]) ->io:format("~s start ",[Key]), 
			   counter_reset(Key),
			   io:format("reset. "),
			   resetRecurser(List);
resetRecurser([])-> ok.

%%Gets a string of the counter needs argument of the key to be counted, bucket is hardcoded
getStringCount(Key) ->
	Count = getCount(Key),
	makeStringCount(Count).
	%strCount = lists:flatten(io_lib:format("~s",[Count])),
	%io_lib:format("\"popularity\": \"~s\"",[[Count]]).

makeStringCount({error,_}) -> "\"popularity\": \"n/a\"";
makeStringCount({_,Count}) ->lists:flatten(io_lib:format("\"popularity\": \"~p\"", [Count])).

%Gets the rating and returns a string to fit the format, needs argument of key with rating, bucket is hardcoded
getRating(Key) -> 
	Com = io_lib:format("GET http://129.16.155.12:10018/buckets/MoviesInfos/keys/~s",[Key]),
	Movieinfo = os:cmd(Com),
	findRating(Movieinfo).

findRating("not found\n") -> "\"rating\": \"n/a\"";
findRating(Movieinfo) ->
	 
	X = string:str(Movieinfo,"imdbRating")+13,
 	Y = string:substr(Movieinfo, X, 3),
        io_lib:format("\"rating\": \"~s\"",[Y]).

%gets a string of the old data argument, bucket is hardcoded and should be identical to the one in the put request of bucketrecurser
getOlddata(Key) ->
	Com =io_lib:format("GET http://129.16.155.12:10018/buckets/Counters_Data/keys/~s",[Key]),
	makeOlddata(os:cmd(Com)).

makeOlddata("{}") -> "";
makeOlddata("not found\n") ->"";
makeOlddata("not found")->"";
makeOlddata(Data) -> Data.

%Resets the counter by incrementing them with minus current counter via put request. Bucket is hardcoded and should be the same as count() and getCount() 
counter_reset(Key) -> 
Keystone= io_lib:format("~s",[Key]),
CounterKey = [37]++["23"]++ tl(hd(Keystone)),
 {_,Number} = getCount(Key),
Nrstring = lists:flatten(io_lib:format("~p",[Number])),
Com = io_lib:format("curl -XPUT http://129.16.155.12:10018/buckets/HashtagsTest1/counters/~s -d -",[CounterKey]),
Command = Com ++ Nrstring, 
os:cmd(Command). 

%%Input needs to be of format <<"#Variable">>, bucket is hardcoded

count(Hashtag) -> 
RHP = get_riak_hostport(riak1),
{ok, R} = riakc_pb_socket:start(RHP#hostport.host, RHP#hostport.port), 
riakc_pb_socket:counter_incr(R,<<"HashtagsTest1">>, Hashtag, 1).


%%Bucket is hardcoded, gets count from the bucket 

getCount(Hashtag) ->
RHP = get_riak_hostport(riak1),
{ok, R} = riakc_pb_socket:start(RHP#hostport.host, RHP#hostport.port),
riakc_pb_socket:counter_val(R,<<"HashtagsTest1">>, Hashtag).



%Requests all keys in a bucket

bucketRequest(Bucket) ->
	RHP = get_riak_hostport(riak1),
	{ok, R} = riakc_pb_socket:start(RHP#hostport.host, RHP#hostport.port),
	riakc_pb_socket:list_keys(R,Bucket).


