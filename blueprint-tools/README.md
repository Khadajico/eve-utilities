# Blueprint Tools
The tool currently offers two actions:

## Locate a blueprint
Look through the list of blueprints in the current EVE dataset, finding all blueprints with a name containing the search string supplied.

`java -jar blueprint-tools.jar -locate "Iron"`

## Optimize a blueprint
Using the supplied blueprint name, will query the CREST API for the latest market prices for all the components parts, and produce a list of the items that you should buy or build depending on the prices/costs of each.

`java -jar blueprint-tools.jar -optimize "Avatar"`

## TODO
- The current project will output the prices without any modification for blueprint quality or taxes.
- The skills of any characters also will not be taken into account.
- Costs associated with invention or production also need to be calculated.
- The current market is fixed to Jita.

## Legal Bits
The various eve-*.json data files contain information derived from the Static Data Export provided by CCP.

© 2014 CCP hf. All rights reserved. "EVE", "EVE Online", "CCP", and all related logos and images are trademarks or registered trademarks of CCP hf.
