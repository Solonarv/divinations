# Plans

## General mechanics

There is a web of ley nodes, spanning the entire world. Each node has a size and is connected to nearby
nodes according to the following rules:
 - a node has any number of incoming connections
 - a node forms 2-4 outgoing connections
 - each outgoing connection goes to another node with lesser size that maximizes size/distance^2

It is made of a form of energy called Leylight. Leylight cannot be lost, created, or even stored;
devices and items are powered by it, but do not consume it.

The web is high in the sky (y=256) and is visible from above cloud height (y>=128).

## Starting out

Travel above the clouds, look up & rightclick a book -> get guide/log-book

Travel up to build limit and look at a node from up close

## Materials

**Ley-charged metals/gems**: various metals and gems, infused with ley light by throwing
into a ley node

**Vitreous Leylight:** a block that naturally generates under some ley nodes.
Fully transparent (like glass), emits light.

## Devices

### Leylight manipulation

Leylight can be drained from and injected into the web, traveling in the form of beams of light.

**Ley taps** attach to a ley node and drain it, sending the Leylight down to the earth.

**Ley spikes** attach to a ley node and force incoming Leylight into it.

**Ley mirrors** reflect a Leylight beam, changing its direction.

**Ley prisms** refract and reflect a Leylight beam, splitting it in two. The ration depends on the angles.

## Transportation

Transportation uses similarities between nodes, connections, and faces.

Two *nodes* are similar if they have the same number of connections, at roughly the same angles,
to nodes of roughly the same size relative to the central node.

Two *edges* are similar if their endpoints have roughly the same ratio of sizes, roughly the same distance,
and further connections at roughly the same angles.

Two *faces* are similar if they have the same number of nodes, the internal angles in order
are roughly the same, and the node sizes in order are roughly the same.

### Jumps

Jumps use a connection between two similar *faces* of the web. At least one of the starting point
and destination must have **Jump Pylons** set up, one under each node and connected with a ring of Leylight.

Upon activation, the user shoots into the sky as a bolt of Leylight, is transported to the corresponding (by similarity) point
in the destination face, and then falls out of the sky.

### Gates

Gates are permanent portals between two locations based on similar connections. To create a Gate, two
**Gate Pylons** must be suspended under the nodes. Each pylon acts as both a ley tap and a ley spike.
These beams of Leylight, together with the original connection and the bottom of the world, make up the
frame of the Gate. These pylons must be set up at both source and destination.

Any entity that passes through one side of the Gate will come out of the other one at the corresponding location.
If there is no space at the destination, the entity will bounce back.

### Tunnels

A tunnel connects two or more similar nodes. The nodes to connect must have a Tunnel Former attached,
which connects via vertical Leylight beam to a Tunnel Endpoint. Any items or fluids entering the endpoint will
come out of another connected endpoint.

### Lightriding

Using special equipment it is possible to become tangible enough to Leylight that one can "ride" the network.
While riding, the player sees the nearby web as well as distant similar nodes, and can travel to any of them.
