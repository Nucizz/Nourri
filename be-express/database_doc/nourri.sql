-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Dec 28, 2023 at 01:53 PM
-- Server version: 5.7.39
-- PHP Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Nourri`
--

-- --------------------------------------------------------

--
-- Table structure for table `ingredient`
--

CREATE TABLE `ingredient` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `ccal` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `ingredient`
--

INSERT INTO `ingredient` (`id`, `name`, `ccal`) VALUES
(1, 'Beef', 1.55882),
(2, 'Cabbage', 0.305),
(3, 'Carrot', 0.383333),
(4, 'Chicken', 1.40741),
(5, 'Cucumber', 0.14),
(6, 'Egg', 1.36364),
(7, 'Eggplant', 0.196667),
(8, 'Leek', 0.306667),
(9, 'Onion', 0.333333),
(10, 'Pork', 1.06195),
(11, 'Potato', 1.24),
(12, 'Radish', 0.2),
(13, 'Tomato', 0.2);

-- --------------------------------------------------------

--
-- Table structure for table `recipe`
--

CREATE TABLE `recipe` (
  `id` int(11) NOT NULL,
  `title` varchar(64) NOT NULL,
  `ingredients` varchar(2560) NOT NULL,
  `instructions` varchar(2560) NOT NULL,
  `summary` varchar(2560) NOT NULL,
  `raw` varchar(8192) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `recipe`
--

INSERT INTO `recipe` (`id`, `title`, `ingredients`, `instructions`, `summary`, `raw`) VALUES
(1, 'Hearty Beef and Vegetable Stew', '- 200g Beef (311.764 calories)\n- 1 Carrot (0.383 calories)\n- 1/4 small Cabbage (0.076 calories)\n- 1/2 Onion (0.167 calories)\n- 1 Leek (0.307 calories)\n- 1 Potato (1.24 calories)\n- 1 Tomato (0.2 calories)\n- Seasonings (salt, pepper, herbs, etc.)', '1. Cut the beef into small pieces and season with salt and pepper.\n2. Heat a large pot over medium-high heat and add the beef. Cook until browned on all sides.\n3. Remove the beef from the pot and set aside.\n4. In the same pot, add the onion, carrot, leek, and cabbage. Sauté for 5 minutes until slightly softened.\n5. Add the beef back to the pot and pour enough water to cover the ingredients. Bring to a boil.\n6. Reduce the heat to low and simmer for 1 hour or until the beef is tender.\n7. Add the potato and tomato to the pot and continue simmering for another 15 minutes or until the potato is cooked.\n8. Season with herbs and any additional desired seasonings.', 'This Hearty Beef and Vegetable Stew is a nutritious and filling meal. It provides a good balance of protein from the beef, vitamins, and minerals from the assortment of vegetables, and energy from the potato. The dish is high in calories due to the beef and potato, but it is also packed with essential nutrients. To make it healthier, you can choose lean beef cuts and adjust the serving size according to your dietary needs. Enjoy this delicious and nourishing stew as a satisfying main course.', 'Title: Hearty Beef and Vegetable Stew\n\nIngredients:\n- 200g Beef (311.764 calories)\n- 1 Carrot (0.383 calories)\n- 1/4 small Cabbage (0.076 calories)\n- 1/2 Onion (0.167 calories)\n- 1 Leek (0.307 calories)\n- 1 Potato (1.24 calories)\n- 1 Tomato (0.2 calories)\n- Seasonings (salt, pepper, herbs, etc.)\n\nInstructions:\n1. Cut the beef into small pieces and season with salt and pepper.\n2. Heat a large pot over medium-high heat and add the beef. Cook until browned on all sides.\n3. Remove the beef from the pot and set aside.\n4. In the same pot, add the onion, carrot, leek, and cabbage. Sauté for 5 minutes until slightly softened.\n5. Add the beef back to the pot and pour enough water to cover the ingredients. Bring to a boil.\n6. Reduce the heat to low and simmer for 1 hour or until the beef is tender.\n7. Add the potato and tomato to the pot and continue simmering for another 15 minutes or until the potato is cooked.\n8. Season with herbs and any additional desired seasonings.\n\nSummary:\nThis Hearty Beef and Vegetable Stew is a nutritious and filling meal. It provides a good balance of protein from the beef, vitamins, and minerals from the assortment of vegetables, and energy from the potato. The dish is high in calories due to the beef and potato, but it is also packed with essential nutrients. To make it healthier, you can choose lean beef cuts and adjust the serving size according to your dietary needs. Enjoy this delicious and nourishing stew as a satisfying main course.'),
(2, 'Grilled Eggplant and Chicken Salad', '- 1 large eggplant (38.0 ccal)\n- 2 skinless, boneless chicken breasts (281.5 ccal)\n- 1 cup radish, sliced (13.3 ccal)\n- 2 tomatoes, diced (34.0 ccal)\n- 2 hard-boiled eggs, sliced (196.0 ccal)', '1. Preheat the grill to medium-high heat.\n2. Slice the eggplant into 1/2-inch thick rounds.\n3. Season the chicken breasts with salt and pepper.\n4. Grill the eggplant slices and chicken breasts until cooked through, about 4-5 minutes per side.\n5. Remove the grilled eggplant and chicken from the grill and let them cool.\n6. Cut the grilled eggplant into bite-sized pieces and transfer to a large salad bowl.\n7. Slice the grilled chicken into thin strips and add to the bowl.\n8. Add the sliced radish, diced tomatoes, and sliced hard-boiled eggs to the bowl.\n9. Toss all the ingredients together until well combined.\n10. Serve the salad immediately or refrigerate until ready to serve.', 'This Grilled Eggplant and Chicken Salad is a healthy and nutritious recipe that is packed with vitamins, minerals, and protein. The grilled eggplant adds a smoky flavor and a good dose of fiber, while the chicken provides lean protein. Radish and tomato contribute additional vitamins and minerals. The hard-boiled eggs add richness and extra protein to the salad. This salad is light, refreshing, and satisfying, making it a perfect choice for a healthy meal.\n\nNutrition Information:\n- Total Calories: 563.9 ccal\n- Calories per serving (1 serving): 282 ccal (based on 2 servings)\n- Fat: 13.4g\n- Carbohydrates: 22.9g\n- Protein: 47.8g\n- Fiber: 9.0g\n- Sugars: 11.3g\n- Sodium: 278.4mg\n\nPlease note that the nutrition information provided is an estimate and may vary depending on the specific brands or types of ingredients used.', 'Title: Grilled Eggplant and Chicken Salad\n\nIngredients:\n- 1 large eggplant (38.0 ccal)\n- 2 skinless, boneless chicken breasts (281.5 ccal)\n- 1 cup radish, sliced (13.3 ccal)\n- 2 tomatoes, diced (34.0 ccal)\n- 2 hard-boiled eggs, sliced (196.0 ccal)\n\nInstructions:\n1. Preheat the grill to medium-high heat.\n2. Slice the eggplant into 1/2-inch thick rounds.\n3. Season the chicken breasts with salt and pepper.\n4. Grill the eggplant slices and chicken breasts until cooked through, about 4-5 minutes per side.\n5. Remove the grilled eggplant and chicken from the grill and let them cool.\n6. Cut the grilled eggplant into bite-sized pieces and transfer to a large salad bowl.\n7. Slice the grilled chicken into thin strips and add to the bowl.\n8. Add the sliced radish, diced tomatoes, and sliced hard-boiled eggs to the bowl.\n9. Toss all the ingredients together until well combined.\n10. Serve the salad immediately or refrigerate until ready to serve.\n\nSummary:\nThis Grilled Eggplant and Chicken Salad is a healthy and nutritious recipe that is packed with vitamins, minerals, and protein. The grilled eggplant adds a smoky flavor and a good dose of fiber, while the chicken provides lean protein. Radish and tomato contribute additional vitamins and minerals. The hard-boiled eggs add richness and extra protein to the salad. This salad is light, refreshing, and satisfying, making it a perfect choice for a healthy meal.\n\nNutrition Information:\n- Total Calories: 563.9 ccal\n- Calories per serving (1 serving): 282 ccal (based on 2 servings)\n- Fat: 13.4g\n- Carbohydrates: 22.9g\n- Protein: 47.8g\n- Fiber: 9.0g\n- Sugars: 11.3g\n- Sodium: 278.4mg\n\nPlease note that the nutrition information provided is an estimate and may vary depending on the specific brands or types of ingredients used.'),
(3, 'Pork and Cucumber Stir-Fry with Tomato and Egg', '- 150g pork (159.29 kcal)\n- 1 medium-sized cucumber (10.08 kcal)\n- 2 tomatoes (0.40 kcal)\n- 2 eggs (2.73 kcal)', '1. Cut the pork into thin slices and set aside.\n2. Peel the cucumber and cut it into thin strips.\n3. Remove the stem and core of the tomatoes, then chop them into small pieces.\n4. Crack the eggs into a bowl and beat them lightly with a fork.\n5. Heat a non-stick pan over medium heat and add the pork. Cook until browned and cooked through.\n6. Move the cooked pork to one side of the pan and add the beaten eggs to the other side. Scramble the eggs until fully cooked.\n7. Add the cucumber and tomatoes to the pan and stir-fry for about 2-3 minutes until they are slightly softened.\n8. Season with salt and pepper or any desired seasonings.\n9. Remove from heat and serve hot.', 'This Pork and Cucumber Stir-Fry with Tomato and Egg recipe is a healthy and nutritious option. Cucumbers are low in calories and provide hydration due to their high water content. Pork is a good source of protein and essential nutrients. Tomatoes are rich in vitamins and minerals, including vitamin C and potassium. Eggs are packed with protein and various vitamins and minerals.\n\nOverall, this dish offers a balanced combination of protein, vitamins, and minerals, making it a nutritious meal option. The recipe uses minimal oil and incorporates a variety of fresh ingredients. Seasonings can be adjusted to personal taste preferences. Enjoy this flavorful and healthful stir-fry as a satisfying main course.', 'Title: Pork and Cucumber Stir-Fry with Tomato and Egg\n\nIngredients:\n- 150g pork (159.29 kcal)\n- 1 medium-sized cucumber (10.08 kcal)\n- 2 tomatoes (0.40 kcal)\n- 2 eggs (2.73 kcal)\n\nInstructions:\n1. Cut the pork into thin slices and set aside.\n2. Peel the cucumber and cut it into thin strips.\n3. Remove the stem and core of the tomatoes, then chop them into small pieces.\n4. Crack the eggs into a bowl and beat them lightly with a fork.\n5. Heat a non-stick pan over medium heat and add the pork. Cook until browned and cooked through.\n6. Move the cooked pork to one side of the pan and add the beaten eggs to the other side. Scramble the eggs until fully cooked.\n7. Add the cucumber and tomatoes to the pan and stir-fry for about 2-3 minutes until they are slightly softened.\n8. Season with salt and pepper or any desired seasonings.\n9. Remove from heat and serve hot.\n\nSummary:\nThis Pork and Cucumber Stir-Fry with Tomato and Egg recipe is a healthy and nutritious option. Cucumbers are low in calories and provide hydration due to their high water content. Pork is a good source of protein and essential nutrients. Tomatoes are rich in vitamins and minerals, including vitamin C and potassium. Eggs are packed with protein and various vitamins and minerals.\n\nOverall, this dish offers a balanced combination of protein, vitamins, and minerals, making it a nutritious meal option. The recipe uses minimal oil and incorporates a variety of fresh ingredients. Seasonings can be adjusted to personal taste preferences. Enjoy this flavorful and healthful stir-fry as a satisfying main course.'),
(4, 'Protein-Packed Veggie Scramble', '- 4 eggs (1.36364 calories/gram) - 304 calories total', '1. Heat a non-stick skillet over medium heat.\n2. Crack the eggs into a bowl and whisk them together until well beaten.\n3. Pour the beaten eggs into the skillet and let them cook for about a minute until slightly set.\n4. Add the following vegetables to the skillet:\n   - 1/2 cup diced red bell pepper - 19 calories\n   - 1/2 cup diced zucchini - 10 calories\n   - 1/2 cup diced spinach - 7 calories\n5. Stir the vegetables into the eggs and continue cooking for another 2-3 minutes until the eggs are fully cooked and the vegetables are tender.\n6. Season with salt, pepper, and any other desired seasonings.\n7. Remove the scramble from the heat and serve hot.', 'This Protein-Packed Veggie Scramble is a nutritious and delicious recipe. The combination of eggs and vegetables makes it a high-protein meal that is also rich in essential vitamins and minerals. The scramble is a great way to start your day as it provides long-lasting energy and keeps you satisfied until your next meal. With a total of 340 calories, this recipe is a healthy option for breakfast or brunch.\n\nNutrition Information:\n- Total calories: 340 calories\n- Protein: 28 grams\n- Carbohydrates: 12 grams\n- Fat: 21 grams\n- Fiber: 3 grams\n- Vitamin C: 90% of the daily recommended intake\n- Vitamin A: 100% of the daily recommended intake\n- Iron: 15% of the daily recommended intake', 'Title: Protein-Packed Veggie Scramble\n\nIngredients:\n- 4 eggs (1.36364 calories/gram) - 304 calories total\n\nInstructions:\n1. Heat a non-stick skillet over medium heat.\n2. Crack the eggs into a bowl and whisk them together until well beaten.\n3. Pour the beaten eggs into the skillet and let them cook for about a minute until slightly set.\n4. Add the following vegetables to the skillet:\n   - 1/2 cup diced red bell pepper - 19 calories\n   - 1/2 cup diced zucchini - 10 calories\n   - 1/2 cup diced spinach - 7 calories\n5. Stir the vegetables into the eggs and continue cooking for another 2-3 minutes until the eggs are fully cooked and the vegetables are tender.\n6. Season with salt, pepper, and any other desired seasonings.\n7. Remove the scramble from the heat and serve hot.\n\nSummary:\nThis Protein-Packed Veggie Scramble is a nutritious and delicious recipe. The combination of eggs and vegetables makes it a high-protein meal that is also rich in essential vitamins and minerals. The scramble is a great way to start your day as it provides long-lasting energy and keeps you satisfied until your next meal. With a total of 340 calories, this recipe is a healthy option for breakfast or brunch.\n\nNutrition Information:\n- Total calories: 340 calories\n- Protein: 28 grams\n- Carbohydrates: 12 grams\n- Fat: 21 grams\n- Fiber: 3 grams\n- Vitamin C: 90% of the daily recommended intake\n- Vitamin A: 100% of the daily recommended intake\n- Iron: 15% of the daily recommended intake');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ingredient`
--
ALTER TABLE `ingredient`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `recipe`
--
ALTER TABLE `recipe`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ingredient`
--
ALTER TABLE `ingredient`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `recipe`
--
ALTER TABLE `recipe`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
