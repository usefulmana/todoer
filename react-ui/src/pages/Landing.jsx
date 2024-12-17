// src/pages/Landing.jsx
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Container,
  Heading,
  Text,
  Stack,
  List,
  ListItem,
  ListIcon,
  VStack,
  useColorMode,
} from '@chakra-ui/react';
import { FaCheckCircle } from 'react-icons/fa';

const LandingPage = () => {
  const { colorMode } = useColorMode();
  const navigate = useNavigate();

  const features = [
    'Simple and intuitive task management',
    'Cross-platform synchronization',
    'Smart task categorization',
    'Priority-based organization',
    'Deadline reminders and notifications',
  ];

  return (
    <>
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
            <Button 
              size="lg" 
              colorScheme="blue"
              onClick={() => navigate('/auth')}
            >
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
                <Button 
                  colorScheme="blue" 
                  size="lg" 
                  w="full"
                  onClick={() => navigate('/auth')}
                >
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
    </>
  );
};

export default LandingPage;