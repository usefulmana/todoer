// App.jsx
import React from 'react';
import {
  Box,
  Button,
  Container,
  Flex,
  Heading,
  Text,
  Stack,
  useColorMode,
  IconButton,
  List,
  ListItem,
  ListIcon,
  VStack,
  HStack,
} from '@chakra-ui/react';
import { MoonIcon, SunIcon } from '@chakra-ui/icons';
import { FaCheckCircle } from 'react-icons/fa';

function App() {
  const { colorMode, toggleColorMode } = useColorMode();

  const features = [
    'Simple and intuitive task management',
    'Cross-platform synchronization',
    'Smart task categorization',
    'Priority-based organization',
    'Deadline reminders and notifications',
  ];

  return (
    <Box minH="100vh">
      {/* Navigation */}
      <Flex
        as="nav"
        align="center"
        justify="space-between"
        wrap="wrap"
        padding={6}
        bg={colorMode === 'light' ? 'white' : 'gray.800'}
        borderBottom="1px"
        borderColor={colorMode === 'light' ? 'gray.200' : 'gray.700'}
      >
        <Heading as="h1" size="lg" letterSpacing="tight">
          Todoer
        </Heading>
        <HStack spacing={4}>
          <IconButton
            icon={colorMode === 'light' ? <MoonIcon /> : <SunIcon />}
            onClick={toggleColorMode}
            variant="ghost"
            aria-label="Toggle dark mode"
          />
          <Button variant="outline" colorScheme="blue" size="md">
            Sign In
          </Button>
        </HStack>
      </Flex>

      {/* Hero Section */}
      <Container maxW="container.xl" py={20}>
        <Stack
          spacing={8}
          alignItems="center"
          textAlign="center"
        >
          <Box
            as="h2"
            fontSize={['4xl', '5xl', '6xl']}
            fontWeight="bold"
            lineHeight="1.2"
            bgGradient="linear(to-r, blue.400, purple.500)"
            bgClip="text"
            pb={2}
          >
            Organize Your Life with Todoer
          </Box>
          <Text fontSize="xl" maxW="2xl">
            The smart todo app that helps you stay productive and organized.
            Manage your tasks effortlessly across all your devices.
          </Text>
          <Stack direction={['column', 'row']} spacing={4}>
            <Button size="lg" colorScheme="blue">
              Get Started Free
            </Button>
            <Button size="lg" variant="outline">
              Learn More
            </Button>
          </Stack>
        </Stack>
      </Container>

      {/* Features Section */}
      <Box bg={colorMode === 'light' ? 'gray.50' : 'gray.900'} py={20}>
        <Container maxW="container.xl">
          <VStack spacing={12}>
            <Heading as="h3" size="xl">
              Why Choose Todoer?
            </Heading>
            <Stack
              direction={['column', 'row']}
              spacing={10}
              align="flex-start"
              w="full"
            >
              <List spacing={6} flex="1">
                {features.map((feature, index) => (
                  <ListItem key={index} display="flex" alignItems="center">
                    <ListIcon as={FaCheckCircle} color="blue.500" />
                    <Text fontSize="lg">{feature}</Text>
                  </ListItem>
                ))}
              </List>
              <Box
                flex="1"
                bg={colorMode === 'light' ? 'white' : 'gray.800'}
                p={8}
                borderRadius="lg"
                boxShadow="xl"
              >
                <Heading size="md" mb={4}>
                  Start Organizing Today
                </Heading>
                <Text mb={4}>
                  Join thousands of users who have transformed their productivity
                  with Todoer. Try it free for 30 days.
                </Text>
                <Button colorScheme="blue" size="lg" w="full">
                  Start Free Trial
                </Button>
              </Box>
            </Stack>
          </VStack>
        </Container>
      </Box>

      {/* Footer */}
      <Box
        as="footer"
        bg={colorMode === 'light' ? 'gray.100' : 'gray.800'}
        py={8}
      >
        <Container maxW="container.xl">
          <Text textAlign="center">
            © {new Date().getFullYear()} Todoer. All rights reserved.
          </Text>
        </Container>
      </Box>
    </Box>
  );
}

export default App;