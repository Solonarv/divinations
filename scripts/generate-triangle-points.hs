{-# LANGUAGE
    LambdaCase,
    DeriveFunctor,
    ScopedTypeVariables
    #-}
module Main where

import System.Random

import Text.Read (readMaybe)
import Data.Char

import Control.Applicative
import Control.Monad
import Data.Maybe

main = showUsage >> loop evalCommand

-- | Repeat a monadic action for as long as its value is true.
loop :: Monad m => m Bool -> m ()
loop act = act >>= \case
    True -> loop act
    False -> pure ()

type Point = V2 Double
data V2 a = V2 {v2x :: a, v2y :: a}
    deriving (Eq, Ord, Functor)

instance Show a => Show (V2 a) where
    show (V2 x y) = show (x,y)
instance Applicative V2 where
    pure a = V2 a a
    V2 f g <*> V2 x y = V2 (f x) (g y)
instance Num a => Num (V2 a) where
    (+) = liftA2 (+)
    (*) = liftA2 (*)
    abs = fmap abs
    signum = fmap signum
    fromInteger = pure . fromInteger
    negate = fmap negate

infixr 7 .*
(.*) :: Num a => a -> V2 a -> V2 a
a .* (V2 x y) = V2 (a * x) (a * y)

readVec :: Read a => String -> Maybe (V2 a)
readVec s = readMaybe s <&> uncurry V2
  where
    (<&>) = flip fmap

evalCommand :: IO Bool
evalCommand = words . map toLower <$> getLine >>= \case
    "exit":_ -> False <$ display "exiting!"
    p0:p1:p2:[] -> fromMaybe showUsage $ do
        v0 <- readVec p0
        v1 <- readVec p1
        v2 <- readVec p2
        pure $ generatePoint (v0, v1, v2) >>= display.show
    _ -> showUsage

showUsage :: IO Bool
showUsage = display $ concat [
    "Enter three points in the format '(x0,y0) (x1,y1) (x2,y2)'\n",
    "to generate a point within the triangle described by those points.\n",
    "No spaces are allowed in the individual coordinate pairs.\n",
    "The generated points will be uniformly distributed within the triangle."]
        

generatePoint :: (Point, Point, Point) -> IO Point
generatePoint (v0, v1, v2) = do
    r0 <- randomRIO (0, 1)
    r1 <- randomRIO (0, 1)
    let 
        u = min r0 r1
        mx = max r0 r1
        v = if mx + u > 1 then mx - u else mx
        vU = v1 - v0
        vV = v2 - v0
    pure $ v0 + u .* vU + v .* vV
        

display :: String -> IO Bool
display msg = True <$ putStrLn msg